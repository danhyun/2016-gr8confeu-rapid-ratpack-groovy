@Grab('io.ratpack:ratpack-groovy:1.3.3')

import static ratpack.groovy.Groovy.ratpack
import ratpack.http.MutableHeaders

import groovy.transform.CompileStatic
import ratpack.groovy.handling.GroovyContext
import ratpack.groovy.handling.GroovyHandler

@CompileStatic
class CORSHandler extends GroovyHandler { // <1>
  @Override
  protected void handle(GroovyContext context) {
    MutableHeaders headers = context.response.headers
    headers.set('Access-Control-Allow-Origin', '*')
    headers.set('Access-Control-Allow-Headers', 'x-requested-with, origin, content-type, accept')
    context.next()
  }
}

ratpack {
  handlers {
    all(new CORSHandler()) // <2>
    get {
      render 'Hej GR8Conf EU 2016!'
    }
  }
}
