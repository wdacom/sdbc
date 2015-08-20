package com.rocketfuel

import org.slf4j.LoggerFactory

/**
 * Created by Jeff on 3/23/2015.
 */
trait Logging {

  val logger = LoggerFactory.getLogger(getClass)

}
