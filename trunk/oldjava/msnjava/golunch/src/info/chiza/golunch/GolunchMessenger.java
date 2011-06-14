package info.chiza.golunch;

import net.sf.jml.*;
import net.sf.jml.event.MsnContactListAdapter;
import net.sf.jml.event.MsnMessageAdapter;
import net.sf.jml.event.MsnMessengerAdapter;
import net.sf.jml.impl.MsnMessengerFactory;
import net.sf.jml.message.MsnControlMessage;
import net.sf.jml.message.MsnDatacastMessage;
import net.sf.jml.message.MsnInstantMessage;
import net.sf.jml.message.MsnUnknownMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

/**
 * @auther Daniel Zhou (danithaca@gmail.com)
 * @organization School of Information, University of Michigan
 * Date: Dec 5, 2007
 */
public class GolunchMessenger {

    private final Log log = LogFactory.getLog(this.getClass());

    private class GolunchMessageListener extends MsnMessageAdapter {
        public void instantMessageReceived(MsnSwitchboard switchboard, MsnInstantMessage message, MsnContact contact) {
            //text message received
            String content = message.getContent();
            if (content.equals("wolverine: stop")) {
                stop();
                return;
            }
            logMessage(
                    contact.getEmail().toString(),
                    switchboard.getMessenger().getOwner().getEmail().toString(),
                    "TEXT",
                    content
            );
            switchboard.sendText("ATTENTION: This is a robot!!!\nTo contact Daniel Zhou, please email him at <danithaca@gmail.com>. Or call (734)239-5100");
        }

        public void controlMessageReceived(MsnSwitchboard switchboard, MsnControlMessage message, MsnContact contact) {
            // such as typing or recording
            logMessage(
                    contact.getEmail().toString(),
                    switchboard.getMessenger().getOwner().getEmail().toString(),
                    "CONTROL",
                    message.toString()
            );
        }

        public void datacastMessageReceived(MsnSwitchboard switchboard, MsnDatacastMessage message, MsnContact contact) {
            //such as Nudge
            logMessage(
                    contact.getEmail().toString(),
                    switchboard.getMessenger().getOwner().getEmail().toString(),
                    "DATACAST",
                    message.toString()
            );
        }

        public void unknownMessageReceived(MsnSwitchboard switchboard, MsnUnknownMessage message, MsnContact contact) {
            logMessage(
                    contact.getEmail().toString(),
                    switchboard.getMessenger().getOwner().getEmail().toString(),
                    "UNKNOWN",
                    message.getContentAsString()
            );
            super.unknownMessageReceived(switchboard, message, contact);
        }
    }

    private class GolunchContactListListener extends MsnContactListAdapter{

        public void contactListInitCompleted(MsnMessenger messenger) {
            //get contacts in allow list
            MsnContact[] contacts = messenger.getContactList().getContactsInList(MsnList.AL);
            for (MsnContact contact : contacts) {
                if (contact.getStatus() != MsnUserStatus.OFFLINE) {
                    // do something
                }
            }
        }

        public void contactStatusChanged(MsnMessenger messenger, MsnContact contact) {
            logStatusChange(
                    contact.getEmail().toString(),
                    messenger.getOwner().getEmail().toString(),
                    contact.getDisplayName(),
                    contact.getPersonalMessage(),
                    contact.getStatus().toString()
            );
            //this is the simplest way to send text
            //messenger.sendText(contact.getEmail(), "hello");
        }
    }

    private class GolunchMessengerListener extends MsnMessengerAdapter {
        public void loginCompleted(MsnMessenger messenger) {
            log.info(messenger.getOwner().getEmail() + " login");
            //messenger.getOwner().setDisplayName("Go.Lunch");
            messenger.getOwner().setPersonalMessage("this is a robot");
        }

        public void logout(MsnMessenger messenger) {
            log.info(messenger.getOwner().getEmail() + " logout");
        }

        public void exceptionCaught(MsnMessenger messenger, Throwable throwable) {
            log.warn("caught exception: " + throwable);
        }
    }

    private void logMessage(String emailFrom, String emailTo, String type, String message) {
        System.out.println(message);
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO msn_msg(email_from, email_to, type, message) VALUES(?, ?, ?, ?)");
            stmt.setString(1, emailFrom);
            stmt.setString(2, emailTo);
            stmt.setString(3, type);
            stmt.setString(4, message);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Log message at " + new Date());
    }

    private void logStatusChange (String emailFrom, String emailTo, String displayName, String personalMessage, String status) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO msn_status(email_from, email_to, display_name, personal_message, status) VALUES(?, ?, ?, ?, ?)");
            stmt.setString(1, emailFrom);
            stmt.setString(2, emailTo);
            stmt.setString(3, displayName);
            stmt.setString(4, personalMessage);
            stmt.setString(5, status);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Log status change at " + new Date());
    }

    protected void addListeners() {
        me.addMessageListener(new GolunchMessageListener());
        me.addContactListListener(new GolunchContactListListener());
        me.addMessengerListener(new GolunchMessengerListener());
    }

    /**
     * 'me' is my msn account. Proxy pattern.
     */
    private final MsnMessenger me;
    private Connection connection;

    public GolunchMessenger() {
        this("go.lunch@hotmail.com", "wolverine");
    }

    public GolunchMessenger(String user, String password) {
        //create MsnMessenger instance
        me = MsnMessengerFactory.createMsnMessenger(user, password);
    }

    public void start() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/msn?user=root");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //MsnMessenger support all protocols by default
        //me.setSupportedProtocol(new MsnProtocol[] { MsnProtocol.MSNP8 });

        //default init status is online,
        me.getOwner().setInitStatus(MsnUserStatus.AWAY);

        //log incoming message
        me.setLogIncoming(false);
        //log outgoing message
        me.setLogOutgoing(false);

        addListeners();

        me.login();
    }

    public void stop() {
        // TODO: housekeeping work before exit

        me.logout();
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        GolunchMessenger messenger;
        if (args.length == 2) {
            messenger = new GolunchMessenger(args[0], args[1]);
        } else {
            messenger = new GolunchMessenger();
        }
        messenger.start();
    }
}
