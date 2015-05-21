# XML Validator Online

A web application for XML learners to validate DTD, XML Schema.
Plus it offers XPath evaluation function on your browser.

Location: [http://xmlvalidator.new-studio.org](http://xmlvalidator.new-studio.org)

## Installation

1. Download JDK7 (8 is not yet supported by Google).
2. Download Google App Engine SDK for Java. Remember to set `APPENGINE_HOME` environment variable.
3. Git clone this repository.
4. Edit `src/main/webapp/WEB-INF/appengine-web.xml`, fill your own application id.
5. Use Gradle tasks to help you.

## Usage

* `gradlew appengineRun`: Run the test server on localhost.
* `gradlew appengineStop`: Stop the test server on localhost.
* `gradlew appengineUpdate`: Deploy stage on your GAE application.

## Contributing

1. Fork it!
2. Create your feature branch: `git checkout -b feature-new`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin feature-new`
5. Submit a pull request :D

## History

This is my little semester project in college in 2009. Built with Google App Engine.

Because in 2015 Google decided shutdown the Master/Slave datastore app, force me to redeploy this app.
I think it's also a good chance to make this little project open source.

## Credits

2009 Scribe Huang

## License

TODO.
