import ratpack.hikari.HikariModule
import ratpack.jackson.Jackson
import ratpack.exec.Promise

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
    // tag::handlers[]
    path { TodoRepository repository -> // <1> <2>
      byMethod { // <3>
        get { // <4>
          repository.getAll()
            .map(Jackson.&json)
            .then(context.&render)
        }
        post { // <5>
          Promise<TodoModel> todo = parse(Jackson.fromJson(TodoModel))
          todo
            .flatMap(repository.&add)
            .map(Jackson.&json)
            .then(context.&render)
        }
      }
    }
    // end::handlers[]
  }
}
