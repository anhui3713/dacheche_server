package com.imooc.dacheche;

import com.imooc.dacheche.exception.ServerStartException;
import com.imooc.dacheche.net.Server;

public class MainClass {
	public static void main(String[] args) {
		try {
			Server.getServer().start();
		} catch (ServerStartException e) {
			e.printStackTrace();
		}
	}
}
