#!/usr/bin/env sh

cd mongo_dumps

mongorestore -d pimp pimp

if [ $? != 0 ] ; then
	echo "Something went wrong."
else
	echo "Database restored."
fi

