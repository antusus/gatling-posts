package com.pragmatists.gatling

import io.gatling.core.Predef._
import io.gatling.core.json.Jackson
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._

class DefaultSimulation extends Simulation {

  private val httpConf = http
    .baseURL("https://jsonplaceholder.typicode.com")
    .acceptHeader(" application/json")

  private val scn: ScenarioBuilder = scenario("Default Scenario")
    .exec(
      http("Get all posts")
        .get("/posts")
        .check(jsonPath("$[*]").ofType[Map[String, Any]].findAll.saveAs("posts"))
    )
    .exec((session: Session) => {
      val postsMap = session("posts").as[Vector[Map[String, Any]]]
      println(postsMap(0))
      session
    })

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)
}
