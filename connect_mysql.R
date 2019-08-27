rs <- dbSendQuery(mydb, "select * from test")
data <- fetch(rs, n=-1)

library(RMySQL)
library(data.table)

mydb <- dbConnect(MySQL(), user = "", password = "", dbname = "trend", host = "")

w1 <- fread("week1.csv", data.table = F, header = T, stringsAsFactors = F)
w2 <- fread("week2.csv", data.table = F, header = T, stringsAsFactors = F)
w3 <- fread("week3.csv", data.table = F, header = T, stringsAsFactors = F)
w4 <- fread("week4.csv", data.table = F, header = T, stringsAsFactors = F)

dbGetQuery(mydb,"set names utf8")

for(i in 1 : dim(w1)[1]){
  dbSendQuery(mydb, paste0("insert into keywords values ('20180604', ", w1$V1[i], ", '", w1$K_1[i], "')"))
  
  if(i %% 100 == 0)
    print(paste0(i, " / ", dim(w1)[1]))
}

for(i in 1 : dim(w2)[1]){
  dbSendQuery(mydb, paste0("insert into keywords values ('20180611', ", w2$V1[i], ", '", w2$`K-2`[i], "')"))
  
  if(i %% 100 == 0)
    print(paste0(i, " / ", dim(w2)[1]))
}

for(i in 1 : dim(w3)[1]){
  dbSendQuery(mydb, paste0("insert into keywords values ('20180618', ", w3$V1[i], ", '", w3$keyword[i], "')"))
  
  if(i %% 100 == 0)
    print(paste0(i, " / ", dim(w3)[1]))
}

for(i in 1 : dim(w4)[1]){
  dbSendQuery(mydb, paste0("insert into keywords values ('20180625', ", w4$V1[i], ", '", w4$keyword[i], "')"))
  
  if(i %% 100 == 0)
    print(paste0(i, " / ", dim(w4)[1]))
}
