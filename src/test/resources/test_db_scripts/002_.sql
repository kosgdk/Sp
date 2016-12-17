ALTER TABLE test_sp_database.clients          MODIFY referrer   TINYINT   NOT NULL  DEFAULT '1';
ALTER TABLE test_sp_database.order_position   MODIFY quantity   SMALLINT  NOT NULL  DEFAULT '1';
ALTER TABLE test_sp_database.orders           MODIFY weight     SMALLINT  NOT NULL  DEFAULT '0';
ALTER TABLE test_sp_database.products         MODIFY weight     INT       NOT NULL  DEFAULT '0';
ALTER TABLE test_sp_database.sp               MODIFY number     INT       NOT NULL;