package kr.wishtarot.finsys;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "kr.wishtarot.finsys") // 매퍼 인터페이스 패키지 경로
public class FinSysApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinSysApplication.class, args);
	}

}
