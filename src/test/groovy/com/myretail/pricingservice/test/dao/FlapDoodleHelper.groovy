package com.myretail.pricingservice.test.dao

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.util.SocketUtils

import com.mongodb.MongoClient

import de.flapdoodle.embed.mongo.MongodExecutable
import de.flapdoodle.embed.mongo.MongodStarter
import de.flapdoodle.embed.mongo.config.IMongodConfig
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder
import de.flapdoodle.embed.mongo.config.Net
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.process.runtime.Network

/*
 * Spins up an inmemory lightweight mongoDB to be used for running unit tests in the PriceDaoSpec class
 */
class FlapDoodleHelper {
	
	private static MongodExecutable mongodExecutable
	
	/*
	 * Will be setup before any unit test is run
	 */
	static def setup() {
		def ip = "localhost"
		def randomPort = SocketUtils.findAvailableTcpPort()
		
		IMongodConfig mongodConfig = new MongodConfigBuilder()
			.version(Version.Main.PRODUCTION)
			.net(new Net(ip, randomPort, Network.localhostIsIPv6()))
			.build()
		MongodStarter starter = MongodStarter.getDefaultInstance()
		mongodExecutable = starter.prepare(mongodConfig)
		mongodExecutable.start();
		
		MongoTemplate mongoTemplate = new MongoTemplate(new MongoClient(ip, randomPort), "pricing")
		return mongoTemplate
	}
	
	/*
	 * Will be cleaned up after the test suite completes running
	 */
	static def cleanup()
	{
		mongodExecutable.stop()
	}

}
