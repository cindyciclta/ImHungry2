# ImHungry2

# Please ALWAYS create a new branch to work on. 
# Then create a pull request for your branch and merge it to master if there is no merge conflicts! :)

## Fixing MySQL server time zone error

Run the following SQL script within MySQLWorkbench

```mysql
SET @@global.time_zone = '+00:00';

SET @@session.time_zone = '+00:00';
SELECT @@global.time_zone, @@session.time_zone;
```


ef