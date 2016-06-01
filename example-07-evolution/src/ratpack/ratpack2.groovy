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
    bindInstance(new TodoBaseHandler2())
    // tag::registry[]
    bindInstance(new TodoHandler()) // <1>
    // end::registry[]
    bindInstance(new TodoChain2())
  }
  handlers {
    all(CORSHandler)
    insert(TodoChain2)
  }
}
