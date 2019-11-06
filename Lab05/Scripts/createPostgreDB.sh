#! /bin/bash
echo "creating db named ... "$USER"_DB"
createdb -h localhost -p $1025 $USER"_DB"
pg_ctl status
