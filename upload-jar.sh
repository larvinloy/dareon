
#setup git  
git config --global user.email "s3562412@student.rmit.edu.au"
git config --global user.name "Loy Larvin" 

#clone the docker repository
git clone --quiet --branch=master  https://larvinloy:$GITHUB_API_KEY@github.com/larvinloy/docker-dareon
#go into directory and copy data we're interested

rm -f docker-dareon/DareonWebApp-0.0.1-SNAPSHOT.jar
cp target/DareonWebApp-0.0.1-SNAPSHOT.jar docker-dareon/
#add, commit and push files

cd docker-dareon
git add .
git commit -m "Travis build $TRAVIS_BUILD_NUMBER pushed [skip ci] "
git push -fq origin master
echo -e "Done\n"
