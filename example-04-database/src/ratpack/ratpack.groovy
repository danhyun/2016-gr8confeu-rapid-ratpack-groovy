import static ratpack.groovy.Groovy.ratpack
import ratpack.hikari.HikariModule

ratpack {
  bindings { // <1>
    module(HikariModule) { config -> // <2>
      config.dataSourceClassName = 'org.h2.jdbcx.JdbcDataSource' // <3>
      config.addDataSourceProperty('URL', "jdbc:h2:mem:tood;INIT=RUNSCRIPT FROM 'classpath:/init.sql'") // <3>
    }
  }
  handlers {
    all(new CORSHandler())
    get {
      render 'Hej Verden!'
    }
  }
}
