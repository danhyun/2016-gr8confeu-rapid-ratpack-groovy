import ratpack.hikari.HikariModule
import ratpack.jackson.Jackson

import static ratpack.groovy.Groovy.ratpack

ratpack {
  bindings {
    module(HikariModule) { config ->
      config.dataSourceClassName = 'org.h2.jdbcx.JdbcDataSource'
      config.addDataSourceProperty('URL', "jdbc:h2:mem:tood;INIT=RUNSCRIPT FROM 'classpath:/init.sql'")
    }
    module(TodoModule)
  }
  handlers {
    all(new CORSHandler())
    get('blocking') { TodoRepository repository ->
      repository.getAll()
        .map(Jackson.&json)
        .then(context.&render)
    }
  }
}
