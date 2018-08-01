package com.pragmatists.gatling

import io.gatling.core.Predef._
import io.gatling.core.body.StringBody
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import scala.concurrent.duration._

class DefaultSimulation extends Simulation {

  private val httpConf = http
    .baseURL("https://jsonplaceholder.typicode.com")
    .acceptHeader("application/json")

  private val commentsFeeder = separatedValues("feeders/comments.csv", '#')

  private val scn: ScenarioBuilder = scenario("Default Scenario")
    .exec(
      http("Get all posts")
        .get("/posts")
        .check(jsonPath("$[*]").ofType[Map[String, Any]].findAll.saveAs("posts"))
    )
    .exec((session: Session) => {
      // use session expression to debug response
      val postsMap = session("posts").as[Vector[Map[String, Any]]]
      println("============ Sample post from list ============")
      println(postsMap(0))
      session
    })
    .exec(repeat(3) {
      feed(commentsFeeder)
        .exec(
          http("Get one post")
            .get("/posts/${posts.random().id}")
            .check(jsonPath("$.id").saveAs("one_post_id"))
        )
        .pause(10 second, 17 seconds)
        .exec(
          http("Read comments of post [${one_post_id}]")
            .get("/posts/${one_post_id}/comments")
        )
        .pause(8 second, 12 seconds)
        .exec(
          http("Add comment")
            .post("/posts/${one_post_id}/comments")
            .headers(Map(HttpHeaderNames.ContentType -> "application/json; charset=UTF-8"))
            .body(StringBody(
              """{
            "name": "gatling",
            "email": "gatling@test.pl",
            "body": "${body}"
          }""".stripMargin))
            .check(jsonPath("$.body").is("${body}"))
            .check(bodyString.saveAs("new_comment"))
        )
        .pause(8 second, 13 seconds)
        .exec((session: Session) => {
          println("============ New comment ============")
          println(session("new_comment").as[String])
          session
        })
    })

  setUp(
    scn.inject(atOnceUsers(1)) // only one user
    //    scn.inject(rampUsersPerSec(2) to(3) during(180 seconds) randomized) // 2 users start and 3 more will be added for 3 minutes in random intervals
  ).protocols(httpConf)
}
