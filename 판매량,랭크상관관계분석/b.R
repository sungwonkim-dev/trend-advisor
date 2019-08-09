library(tidyverse)
library(plyr)
getwd()
df29 <- read.csv("data/health_20190729.csv")
df30 <- read.csv("data/health_20190730.csv")
df31 <- read.csv("data/health_20190731.csv")

df29_1 <- df29[,-4]
df30_1 <- df30[,-4]

df29_1$purchase <- replace(df29_1$purchase,df29_1$purchase=="",NA)
df29_1$seller <- replace(df29_1$seller,df29_1$seller=="",NA)
df30_1$purchase <- replace(df30_1$purchase,df30_1$purchase=="",NA)
df30_1$seller <- replace(df30_1$seller,df30_1$seller=="",NA)

df29_1 <- na.omit(df29_1)
nrow(df29_1)
df30_1 <- na.omit(df30_1)
nrow(df30_1)

df29_1$purchase <- as.numeric(df29_1$purchase)
df29_1$review <- as.numeric(df29_1$review)
df30_1$purchase <- as.numeric(df30_1$purchase)
df30_1$review <- as.numeric(df30_1$review)

write.csv(df29,file="tmp.csv")

str(df29_1)

df29_1$fac <- paste(df29_1$title,df29_1$seller,sep="<>")
df29_1 <- arrange.vars(df29_1,c("fac"=3))
df29_1 <- df29_1[,-4]
df29_1 <- df29_1[,-4]

df29_1$fac <- as.factor(df29_1$fac)
aaa <- df29_1 %>% 
  group_by(rank,keyword,fac) %>% 
  summarise(purchase=sum(purchase),review = sum(review))


df30_1$fac <- paste(df30_1$title,df30_1$seller,sep="<>")
df30_1 <- arrange.vars(df30_1,c("fac"=3))
df30_1 <- df30_1[,-4]

library(data.table)
df30_1$fac <- as.factor(df30_1$fac)
bbb <- df30_1 %>% 
  group_by(rank,keyword,fac) %>% 
  summarise(purchase=sum(purchase),review = sum(review))


j_29_30 <- inner_join(aaa,bbb,by=c("fac"="fac"))
j_29_30 <- na.omit(j_29_30)

j_29_30 <- rename(j_29_30,
                  purchase29=purchase.x,
                  purchase30=purchase.y,
                  review29=review.x,
                  review30=review.y)
j_29_30 <- rename(j_29_30,
                  rank29=rank.x,
                  keyword29=keyword.x,
                  rank30=rank.y,
                  keyword30=keyword.y)
j_29_30 <- arrange.vars(j_29_30,c("keyword29"=3))

j_29_30 %>% group_by(rank.x,keyword.x) %>%
  summarize(p29=sum(purchase.x),p30=sum(purchase.y),r29=sum(review.x),r30=sum(review.y))

j_29_30$dp <- j_29_30$purchase30-j_29_30$purchase29
j_29_30$dr <- j_29_30$review30-j_29_30$review29

by29 <- j_29_30 %>% group_by(rank29,keyword29) %>% 
  summarize(p29=sum(purchase29),p30=sum(purchase30),r29=sum(review29),r30=sum(review30))
by29$dp <- by29$p30-by29$p29
by29$rank29 <- as.numeric(by29$rank29)
by29 <- arrange(by29,rank29)















dir()
test <- fread("data/health_20190729.csv", data.table = F, header = T, stringsAsFactors = F)
head(test)
str(test)
summary(test)
unique(test$rank)

t1 <- test[-grep("http", test$link), ]
str(test)
str(t1)
head(t1)
