#!/usr/bin/env sh

pushd ../mongo_dumps > /dev/null

mongorestore -d pimp pimp

if [ $? != 0 ] ; then
	echo "Something went wrong."
else
	echo "Database restored."
fi

popd > /dev/null

