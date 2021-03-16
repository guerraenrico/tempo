#!/bin/sh

decrypt() {
  PASSPHRASE=$1
  INPUT=$2
  OUTPUT=$3
  gpg --quiet --batch --yes --decrypt --passphrase="$PASSPHRASE" --output $OUTPUT $INPUT
}

if [ ! -z "$ENCRYPT_KEY" ]
then
  decrypt ${ENCRYPT_KEY} release/keystore.gpg release/keystore.jks
  decrypt ${ENCRYPT_KEY} release/service_account.gpg release/service_account.json
  decrypt ${ENCRYPT_KEY} app/google-services.gpg app/google-services.json
else
  echo "ENCRYPT_KEY is empty"
fi
