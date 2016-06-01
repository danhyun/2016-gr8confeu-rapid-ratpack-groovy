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
    get { TodoRepository repository ->
      repository.getAll()
        .map(Jackson.&json)
        .then(context.&render)
    }
    post { TodoRepository repository ->
      Promise<TodoModel> todo = parse(Jackson.fromJson(TodoModel))
      todo
        .flatMap(repository.&add)
        .map(Jackson.&json)
        .then(context.&render)
    }
    // end::handlers[]
  }
}
