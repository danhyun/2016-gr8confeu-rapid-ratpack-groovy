import ratpack.exec.Promise
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
    // tag::handler[]
    get('blocking') {
      TodoRepository repository = get(TodoRepository.class) // <1>
      Promise<List<TodoModel>> todos = repository.getAll() // <2>
      todos.then{ t -> render(Jackson.json(t)) } // <3>
    }
    // end::handler[]
  }
}
