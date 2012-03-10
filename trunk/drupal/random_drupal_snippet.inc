<?php

// create content type, fields
function reccloud_create_content_type() {
    // config the rec_task "module" content type, which is defined by hook_node_info.
  node_types_rebuild();
  $types = node_type_get_types();
  assert(isset($types['rec_task']));  // this content type has to be existed first.

  node_add_body_field($types['rec_task'], 'Description');
  // Default options: not promoted.
  //variable_set('node_options_rec_task', array('status'));

  // create the "Recommender Task Settings" field using Encrypted Settings
  $field = array(
    'field_name' => 'settings_plain',
    'type' => 'encset', // encrypted setting from encset module
    'entity_types' => array('node'),
    'cardinality' => 1,
    //'locked' => TRUE,  // users can't create instance
    'settings' => array(
      'encryption' => 'none',
      'usage' => 'ini',
    ),
  );
  field_create_field($field);

  // create instances to node.
  $instance = array(
    'field_name' => 'settings_plain',
    'label' => t('Recommender Task Settings'),
    'entity_type' => 'node',
    'bundle' => 'rec_task',
    'description' => t('This field stores settings (database access info, etc) for this recommender task'),
    //'default_value' => array(array('value' => "access_key=\nsecret_key=")),
    'widget' => array(
      'type' => 'encset_textarea',
    ),
    'display' => array(
      'default' => array(
        'label' => 'hidden',
        'type' => 'hidden',
      ),
    )
  );
  field_create_instance($instance);
}


// delete stuff
function reccloud_delete_content_type()
{ // find rec_task node and delete
  $result = db_query('SELECT nid FROM {node} n WHERE n.type = :type', array(':type' => 'rec_task'));
  $nids = $result->fetchCol();
  node_delete_multiple($nids);

  // delete fields
  field_delete_field('settings_plain');

  // delete instances
  $instances = field_info_instances('node', 'rec_task');
  foreach ($instances as $instance_name => $instance) {
    field_delete_instance($instance);
  }

  // Purge all field infromation
  field_purge_batch(1000);
}


// hook_theme
/**
 * Impelements hook_theme().
 */
function reccloud_theme($existing, $type, $theme, $path) {
  return array(
    'server_status' => array(
      'variables' => array(
        'status' => NULL,
        'queue_length' => 0,
      ),
      'template' => 'server_status',
    ),
    'task_status' => array(
      'variables' => array(
        'position' => -1,
      ),
      'template' => 'task_status',
    ),
  );
}
