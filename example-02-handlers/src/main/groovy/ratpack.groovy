@Grab('io.ratpack:ratpack-groovy:1.3.3')

import static ratpack.groovy.Groovy.ratpack
import ratpack.http.MutableHeaders

ratpack {
  handlers {
    get {
      MutableHeaders headers = response.headers // <1>
      headers.set('Access-Control-Allow-Origin', '*') // <2>
      headers.set('Access-Control-Allow-Headers', 'x-requested-with, origin, content-type, accept') // <2>
      render 'Hej GR8Conf EU 2016!'
    }
  }
}
