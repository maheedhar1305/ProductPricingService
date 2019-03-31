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

class FlapDoodleHelper {
	
	private static MongodExecutable mongodExecutable
	
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
	
	static def cleanup()
	{
		mongodExecutable.stop()
	}

}
