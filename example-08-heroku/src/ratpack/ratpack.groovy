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
    bindInstance(new TodoBaseHandler())
    bindInstance(new TodoHandler())
    bindInstance(new TodoChain())
  }
  handlers {
    all(CORSHandler)
    prefix('todo', TodoChain)
  }
}
