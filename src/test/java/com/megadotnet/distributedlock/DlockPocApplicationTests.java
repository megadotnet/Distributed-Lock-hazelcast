package com.megadotnet.distributedlock;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@Ignore
public class DlockPocApplicationTests {


	@Test
	public void testclient()
	{
		ClientConfig clientConfig = new ClientConfig();
		List<String> addressServer=new ArrayList<>();
		addressServer.add("localhost");
		//clientConfig.getNetworkConfig().setAddresses(addressServer);

		HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);
		IMap map = hazelcastInstance.getMap("employees");
		assertNotNull(map);

		//map.put("01", "Joe");
		assertNotNull(map.get("01"));
		//hazelcastInstance.
	}

}
