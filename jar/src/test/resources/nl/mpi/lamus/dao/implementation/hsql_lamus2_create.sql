CREATE TABLE workspace (
        workspace_id integer GENERATED BY DEFAULT AS IDENTITY (START WITH 1),
        user_id varchar(30) NOT NULL,
        top_node_id integer,
        top_node_archive_uri varchar(255),
        top_node_archive_url varchar(255),
        start_date timestamp NOT NULL,
        end_date timestamp,
        session_start_date timestamp NOT NULL,
        session_end_date timestamp,
        used_storage_space bigint,
        max_storage_space bigint,
        status varchar(255) NOT NULL,
        message varchar(255) NOT NULL,
        archive_info varchar(255),
        PRIMARY KEY (workspace_id));

CREATE TABLE node (
        workspace_node_id integer GENERATED BY DEFAULT AS IDENTITY (START WITH 1),
        workspace_id integer NOT NULL,
        profile_schema_uri varchar(255),
        name varchar(255) NOT NULL,
        title varchar(255),
        type varchar(255) NOT NULL,
        workspace_url varchar(255),
        archive_uri varchar(255),
        archive_url varchar(255),
        origin_url varchar(255),
        status varchar(255) NOT NULL,
        pid varchar(255),
        format varchar(255),
        PRIMARY KEY (workspace_node_id));

CREATE TABLE node_link (
        parent_workspace_node_id integer NOT NULL,
        child_workspace_node_id integer NOT NULL,
        child_uri varchar(255) NOT NULL,
        PRIMARY KEY (parent_workspace_node_id, child_workspace_node_id));

CREATE TABLE node_replacement (
        old_node_id integer NOT NULL,
        new_node_id integer NOT NULL,
        PRIMARY KEY (old_node_id, new_node_id));

ALTER TABLE node ADD CONSTRAINT workspace_node FOREIGN KEY (workspace_id) REFERENCES workspace (workspace_id);

ALTER TABLE node_link ADD CONSTRAINT node_link_parent FOREIGN KEY (parent_workspace_node_id) REFERENCES node (workspace_node_id);
ALTER TABLE node_link ADD CONSTRAINT node_link_child FOREIGN KEY (child_workspace_node_id) REFERENCES node (workspace_node_id);

ALTER TABLE node_replacement ADD CONSTRAINT node_replacement_old FOREIGN KEY (old_node_id) REFERENCES node (workspace_node_id);
ALTER TABLE node_replacement ADD CONSTRAINT node_replacement_new FOREIGN KEY (new_node_id) REFERENCES node (workspace_node_id);