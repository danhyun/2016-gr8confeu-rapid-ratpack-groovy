import ratpack.hikari.HikariModule

import static ratpack.groovy.Groovy.ratpack

ratpack {
  bindings {
    module(HikariModule) { config ->
      config.dataSourceClassName = 'org.h2.jdbcx.JdbcDataSource'
      config.addDataSourceProperty('URL', "jdbc:h2:mem:tood;INIT=RUNSCRIPT FROM 'classpath:/init.sql'")
    }
    module(TodoModule)
    bindInstance(new CORSHandler())
    bindInstance(new TodoBaseHandler()) // <1>
    bindInstance(new TodoChain()) // <2>
  }
  handlers {
    all(CORSHandler)
    insert(TodoChain) // <3>
  }
}
