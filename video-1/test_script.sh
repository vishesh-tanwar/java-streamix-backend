sed -i '' -e 's/.analyzer(/.searchAnalyzer("standard").analyzer(/g' src/main/java/com/yt/backend/video/service/IndexService.java
mvn clean compile
