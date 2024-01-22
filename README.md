Building TeamLab yourself:

1. Install [JDK 8][4].
2. Install [Python 2.7][5].
3. Install [Maven][2].
4. Install [Tomcat 8][3]. Don't forget to follow its readme after you download it.
5. Place the `schema` file and the `groupEng` folder in Tomcat's working directory (usually `$CATALINA_HOME` or `$CATALINA_HOME/bin`).
6. Compile the project using Maven.
```shell
mvn compile war:war
```
7. Place the compiled archive in Tomcat's `webapps` folder.
```shell
cp target/Teamlab.war $CATALINA_HOME/webapps/Teamlab.war
```
8. Run tomcat. It will start the TeamLab webapp automatically.
```shell
$CATALINA_HOME/catalina.sh run
```
TeamLab should now be accessible at `http://localhost:8080/Teamlab`.

[1]: https://www.docker.com/get-started
[2]: https://maven.apache.org/index.html
[3]: https://tomcat.apache.org/download-80.cgi
[4]: https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html
[5]: https://www.python.org/
