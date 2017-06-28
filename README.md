# Dareon
Data Reviews Online spring boot web-app

Data Reviews Online (Dareon) is an application that assists in the process of submission and review of proposals for the inclusion of datasets into a data repository. It helps with the management of Calls for Proposals and the associated proposal review process. Dareon also includes classification schemes to help match Reviewers with appropriate Proposals.

The dockerised version of the Dareon project [docker-dareon](https://github.com/larvinloy/docker-dareon), can be used for deployment.

## Built With

* Spring Boot - The web framework used
* Maven - Dependency Management

## Reconfiguring Scripts for Fork

### To reconfigure continuous deployment
1. Edit repo url in upload-jar.sh to the forked repo url
2. Create GitHub API key and add it to Travis account, follow instructions from this [blog](https://medium.com/@daggerdwivedi/push-your-apk-to-your-github-repository-from-travis-11e397ec430d)
2. Setup Travis account to monitor both repos (dareon and docker-dareon)


## Authors

* **Loy Larvin** - *Initial work* - [larvinloy](https://github.com/larvinloy)
* **Rommel Gaddi** - *Initial work* - [gaddirg](https://github.com/gaddirg)
* **Peitong Wang** - *Initial work* - [s3501054PeitongWang](https://github.com/s3501054PeitongWang)
* **Ayush Garg** - *Initial work* - [s3555116ayushgarg](https://github.com/s3555116ayushgarg)


## License

This project is licensed under the BSD2 License - see the [LICENSE](https://github.com/larvinloy/dareon/blob/master/License) file for details
