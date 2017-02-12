# Installing
* install redis and start it on localhost:6379 

# Running
`sbt run`

# Connecting
* Install (WSC)[https://github.com/raphael/wsc]
* `wsc ws://localhost:9005`
* `nick some_nick`

# Example
Connecting and changing name to foobar, then sending message to yourself:
```
>> nick foobar
send foobar hi
>> send foobar hi
<< hi
```