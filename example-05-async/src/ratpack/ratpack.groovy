import jooq.tables.Todo
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import ratpack.exec.Blocking
import ratpack.exec.Promise
import ratpack.hikari.HikariModule
import ratpack.jackson.Jackson

import javax.sql.DataSource

import static ratpack.groovy.Groovy.ratpack

ratpack {
  bindings {
    module(HikariModule) { config ->
      config.dataSourceClassName = 'org.h2.jdbcx.JdbcDataSource'
      config.addDataSourceProperty('URL', "jdbc:h2:mem:tood;INIT=RUNSCRIPT FROM 'classpath:/init.sql'")
    }
    // tag::todo-module[]
    module(TodoModule)
    // end::todo-module[]
  }
  handlers {
    all(new CORSHandler())
    // tag::jooq[]
    get('blocking') {
      DataSource ds = get(DataSource)
      DSLContext dsl = DSL.using(ds, SQLDialect.H2)
      def select = dsl.select().from(Todo.TODO)
      Promise promise = Blocking.get { // <1>
        select.fetchMaps()
      }
      promise.then { todos -> // <2>
        render(Jackson.json(todos))
      }
    }
    // end::jooq[]
  }
}
