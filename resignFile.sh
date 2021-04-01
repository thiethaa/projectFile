#!/bin/sh
echo Moving licenses to the destination Directory....

mkdir "releaseLicenses"
for f in *.properties; do mv $f releaseLicenses/$f; done

exit