#!/bin/sh

private_key=$(grep -v 'PRIVATE KEY' private.pem)
api_key=$(grep -v 'PUBLIC KEY' public.pem)

host_port="localhost:8080"
base_path="/sample-jersey2-hmac/api/entries"

url_prefix="http://${host_port}"

sign()
{
  method=$1
  timestamp=$2
  request=$3
  content=$4
  data=$(printf %s\\n%s\\n%s "$method" "$timestamp" "$request")
  if [ -n "$content" ]; then
    data=$(printf %s\\n%s "$data" "$content")
  fi

  echo "$data\c" | openssl dgst -sha256 -hmac "$private_key" -binary | base64 | sed -e 's/\+/-/g' | sed -e 's/\//_/g'
}

# GET
timestamp=$(gdate -u +"%Y-%m-%dT%H:%M:%SZ")
request="${base_path}?apiKey=${api_key}"
signature=$(sign GET "$timestamp" "$request")
curl -v --header "X-Auth-Version: 1" \
  --header "X-Auth-Timestamp: ${timestamp}" \
  --header "X-Auth-Signature: ${signature}" \
  "${url_prefix}${request}"

# POST
method="POST"
timestamp=$(gdate -u +"%Y-%m-%dT%H:%M:%SZ")
request="${base_path}?apiKey=${api_key}"
content='{"title":"new entry","description":"new description"}'
signature=$(sign "$method" "$timestamp" "$request" "$content")
curl -v -X "$method" \
  --header "X-Auth-Version: 1" \
  --header "X-Auth-Timestamp: ${timestamp}" \
  --header "X-Auth-Signature: ${signature}" \
  --header 'Content-Type: application/json' \
  "${url_prefix}${request}" \
  -d "$content"

# PUT
method="PUT"
timestamp=$(gdate -u +"%Y-%m-%dT%H:%M:%SZ")
request="${base_path}/55a04c79-1cde-4214-80b0-20d78c3bcfc2?apiKey=${api_key}"
content='{"id":"55a04c79-1cde-4214-80b0-20d78c3bcfc2","title":"updated entry","description":"updated description"}'
signature=$(sign "$method" "$timestamp" "$request" "$content")
curl -v -X "$method" \
  --header "X-Auth-Version: 1" \
  --header "X-Auth-Timestamp: ${timestamp}" \
  --header "X-Auth-Signature: ${signature}" \
  --header 'Content-Type: application/json' \
  "${url_prefix}${request}" \
  -d "$content"
