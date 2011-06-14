package compute;

import java.io.Serializable;

// 必须要支持Serializable，才能将object发送到remote端
public interface Task extends Serializable {
    Object execute();
}
