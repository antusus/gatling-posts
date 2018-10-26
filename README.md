Using Gatling to test REST api provided by Jsonplaceholder
=

This branch is using Gatling 3. The biggest change is that it can run on jdk >= 8 .
You can read on all the changes here:
https://gatling.io/docs/current/migration_guides/2.3-to-3.0/#to-3-0

This project uses Gatling.io (https://gatling.io/) to execute basic load test
against Jsonplaceholder (https://jsonplaceholder.typicode.com/).

Java >= 8 is necessary to run Gatling. 
Follow installation instructions on Gatling page (https://gatling.io/download/).

If you want just to generate report then you can find `simulation.log` 
in `$PROJECT_CHECKOUT_PATH$/simulation_results` folder. 
Check https://gatling.io/docs/current/general/reports/ 
on how to generate report.

There are two simulation files:
 * `com.pragmatists.gatling.InitialSimulation` - basic call to get all posts,
 * `com.pragmatists.gatling.DefaultSimulation` - scenario in which users browse 
 posts, open few and add comments.
 
 To run them first you need to modify configuration in Gatling folder `conf/gatling.conf`.
 There are few entries to be changed, look for section `directory` and modify:
  * resources - `$PROJECT_CHECKOUT_PATH$/src/main/resources`
  * simulations - `$PROJECT_CHECKOUT_PATH$/src/main/scala`

To run `DefaultSimulation` the simulation execute:

`./bin/gatling.sh -s com.pragmatists.gatling.DefaultSimulation`
