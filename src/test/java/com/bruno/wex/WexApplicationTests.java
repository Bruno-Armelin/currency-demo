package com.bruno.wex;

import com.bruno.wex.api.controller.TransactionController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class WexApplicationTests {

	@Autowired
	private TransactionController transactionController;

	@Test
	void contextLoads() {
		assertThat(transactionController).isNotNull();
	}

}
