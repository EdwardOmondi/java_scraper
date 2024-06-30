# Stratalis Web Scraper Test By Edward Omondi

This is my submission for the agent that extracts all the mayors listed for every region on the https://www.mon-maire.fr/maires-regions website using java
The  output file is the `scraper.csv` file in the root directory of the project.

This folder contains the following files:
1. `scraper.csv` - The output file containing the extracted data
2. `pom.xml` - The maven project file
3. `src` - The source code folder
4. `readme.md` - The readme file
5. `mvnw` - The maven wrapper file for Unix
6. `mvnw.cmd` - The maven wrapper file for windows

## Requirements
1. Java 8 or later

## Variables
The  following variables can be changed in the `Main.java` file
1. `regionLimiter` - The number of regions to extract the mayors from. The default value is 10.
2. `mayorLimiter` - The number of mayors to extract from each region. The default value is 10.
3. `pageLimiter` - The number of pages per region to extract the mayors from. The default value is 10.

## Running the project
1. Unzip the project folder
2. Open the terminal and navigate to the project folder
3. Run the command `./mvnw clean install package` or `./mvnw.cmd clean install package` to install the dependencies and build the project 
4. Run the command `./mvnw exec:java` or `./mvnw.cmd exec:java` to run the project

## Dependencies
1. Jsoup - For web scraping
2. Apache Commons CSV - For writing the output to a csv file

## Author
Edward Omondi - [Github](https://github.com/EdwardOmondi)
