sudo yum update -y

wget https://download.oracle.com/java/23/latest/jdk-23_linux-x64_bin.tar.gz


tar -xvzf jdk-23_linux-x64_bin.tar.gz


sudo mv jdk-23.0.1 /usr/lib/jvm/

nano ~/.bash_profile

export JAVA_HOME=/usr/lib/jvm/jdk-23.0.1
export PATH=$JAVA_HOME/bin:$PATH

source ~/.bash_profile

java -version

Install Maven
cd /opt
sudo wget https://archive.apache.org/dist/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.tar.gz
sudo tar -xvzf apache-maven-3.9.9-bin.tar.gz
sudo ln -s /opt/apache-maven-3.9.9 /usr/local/maven

Add Maven’s bin directory to your PATH:
sudo nano /etc/profile.d/maven.sh


Add the following to the file:
export M2_HOME=/opt/apache-maven-3.9.9
export MAVEN_HOME=/opt/apache-maven-3.9.9
export PATH=${M2_HOME}/bin:${PATH}


Make the script executable:
sudo chmod +x /etc/profile.d/maven.sh

Reload environment:
source /etc/profile.d/maven.sh

Verify Maven Installation:
mvn -version

Upload Your Spring Boot Application to EC2

On your local machine, run
mvn clean package

Transfer the JAR to EC2:
Run the Application:
SSH into the EC2 instance and execute the command to start your Spring Boot application
java -jar my-spring-boot-app-0.0.1-SNAPSHOT.jar


Your application should now be running on port 8080. Verify by accessing http://<instance-public-ip>:8080 in a web browser.


Set Up Spring Boot as a Service
Create a Systemd Service File:

Create a new service file for Spring Boot:
sudo nano /etc/systemd/system/springboot-app.service

[Unit]
Description=Spring Boot Application
After=network.target

[Service]
User=ec2-user
ExecStart=/usr/lib/jvm/jdk-23.0.1/bin/ -jar /home/ec2-user/my-spring-boot-app-0.0.1-SNAPSHOT.jar
Restart=always

[Install]
WantedBy=multi-user.target

Reload systemd:
sudo systemctl daemon-reload



Start and Enable the Service:
sudo systemctl start springboot-app
sudo systemctl enable springboot-app


Check Status:
sudo systemctl status springboot-app


Step 8: Set Up Nginx as a Reverse Proxy (Optional)
Install Nginx:

Install Nginx
sudo yum install nginx -y


Configure Nginx:

Edit the Nginx configuration file
sudo nano /etc/nginx/nginx.conf


Add the following to the server block to proxy requests to the Spring Boot app
server {
    listen 80;
    server_name <instance-public-ip>;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}


Restart Nginx:
sudo systemctl restart nginx
sudo systemctl enable nginx


