#!/bin/bash

mysql -uroot -p"$MYSQL_ROOT_PASSWORD" < ./create_db.sql

mysql -uroot -p"$MYSQL_ROOT_PASSWORD" < ./create_tables.sql