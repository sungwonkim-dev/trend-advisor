library(tidyr)
library(data.table)
library(dplyr)
library(ggplot2)
h29 <- fread("data/health_20190729.csv", data.table = F, header = T, stringsAsFactors = F)
h30 <- fread("data/health_20190730.csv", data.table = F, header = T, stringsAsFactors = F)
h31 <- fread("data/health_20190731.csv", data.table = F, header = T, stringsAsFactors = F)

str(h29)
str(h30)
str(h31)

h29$j <- as.character(h29$j)
h29$k <- as.character(h29$k)
h30$j <- as.character(h30$j)
h30$k <- as.character(h30$k)
h31$j <- as.character(h31$j)
h31$k <- as.character(h31$k)

h29 <- clear(h29)
h30 <- clear(h30)
h31 <- clear(h31)
for(i in c(1,2,3,4,5,6)){
  h29[i] <- replace(h29[i],h29[i]=="",NA)
}
for(i in c(1,2,3,4,5,6)){
  h30[i] <- replace(h30[i],h30[i]=="",NA)
}
for(i in c(1,2,3,4,5,6)){
  h31[i] <- replace(h31[i],h31[i]=="",NA)
}
clear <- function(data){
  fin <- data.frame()
  a <- data
  while(length(a) != 7){
    col <- colnames(a[4])
    b <- a[grep("http", a[, col]), ]
    fin <- bind_rows(fin,b[, 1:7])
    c <- a[-grep("http", a[, col]), ]
    c <- c %>% tidyr::unite(title,c("title", "link"),sep="//")
    names(c)[1:7] <- colnames(fin)
    a <- c
  }
  fin <- bind_rows(fin,a[, 1:7])
  return(fin)
}

h29 <- na.omit(h29)
h30 <- na.omit(h30)
h31 <- na.omit(h31)

h29 <- h29[,-7]
h30 <- h30[,-7]
h31 <- h31[,-7]
h29 <- h29[,-4]
h30 <- h30[,-4]
h31 <- h31[,-4]


h29$keyword <- as.factor(h29$keyword)
h30$keyword <- as.factor(h30$keyword)
h31$keyword <- as.factor(h31$keyword)
h29$purchase <- as.numeric(h29$purchase)
h30$purchase <- as.numeric(h30$purchase)
h31$purchase <- as.numeric(h31$purchase)

a <- h29
b <- h30
c <- h31

a <- a %>% unite(title,c("title", "seller"),sep="<>")
b <- b %>% unite(title,c("title", "seller"),sep="<>")
c <- c %>% unite(title,c("title", "seller"),sep="<>")

a <- a[!grepl("#NAME?", a$title),]
b <- b[!grepl("#NAME?", b$title),]
c <- c[!grepl("#NAME?", c$title),]

a$title <- as.factor(a$title)
b$title <- as.factor(b$title)
c$title <- as.factor(c$title)
a <- a %>% 
  group_by(rank,keyword,title) %>% 
  summarise(purchase=sum(purchase))
b <- b %>% 
  group_by(rank,keyword,title) %>% 
  summarise(purchase=sum(purchase))
c <- c %>% 
  group_by(rank,keyword,title) %>% 
  summarise(purchase=sum(purchase))


a <- arrange(a,title)
b <- arrange(b,title)
c <- arrange(c,title)
a <- arrange(a,rank,title)
b <- arrange(b,rank,title)
c <- arrange(c,rank,title)

ab <- inner_join(a,b,by=c("title"="title","keyword"="keyword"))
ab$dp <- ab$purchase.y-ab$purchase.x
ab <- arrange(ab,keyword)
max(ab$dp)
min(ab$dp)
table(ab$dp)
ab <- arrange(ab,dp)
ab29 <- ab %>% group_by(rank.x,keyword) %>% summarize(delta=sum(dp))

bc <- inner_join(b,c,by=c("title"="title","keyword"="keyword"))
bc$dp <- bc$purchase.y-bc$purchase.x
bc <- arrange(bc,keyword)
bc30 <- bc %>% group_by(rank.x,keyword) %>% summarize(delta=sum(dp))

attach(ab29_out)
detach(ab29_out)

ggplot(ab29_out,aes(x=rank.x,y=delta))+geom_point()
ggplot(ab29,aes(x=rank.x,y=delta))+geom_point()
cov(rank.x,delta)
cor(rank.x,delta,method='pearson')
cor.test(rank.x,delta)


abpos <- ab %>% filter(dp>0)
abpos29 <- abpos %>% group_by(rank.x,keyword) %>% summarize(delta=sum(dp))
abpos29 <- abpos29 %>% filter(delta<2000)
ggplot(abpos29,aes(x=rank.x,y=delta))+geom_point()
cor(abpos29$rank.x,abpos29$delta,method='pearson')
cor.test(abpos29$rank.x,abpos29$delta)


ab29_out_1 <- ab29_out %>% filter(delta<2000)
ggplot(ab29_out_1,aes(x=rank.x,y=delta))+geom_point()
cor(ab29_out_1$rank.x,ab29_out_1$delta,method='pearson')
cor.test(ab29_out_1$rank.x,ab29_out_1$delta)


ab29_1 <- ab29 %>% dplyr::filter(delta >(-106)) %>% dplyr::filter(delta <621)
ab29_out <- ab29 %>% dplyr::filter(delta >0)
ab29 <- data.frame(ab29)

boxplot(abpos29$delta)$stats



lmlm <- lm(delta ~ rank.x,data=abpos29)
summary(lmlm)
plot(delta ~ rank.x,data=abpos29)
abline(lmlm,col="red")

ggplot(abpos29,aes(x=rank.x ,y=delta))+geom_point()+
  stat_smooth(method=lm)


abpos29_fix <- abpos29 %>% filter(delta<1250)
lmlm_fix <- lm(delta ~ rank.x,data=abpos29_fix)
summary(lmlm_fix)
plot(delta ~ rank.x,data=abpos29_fix)
abline(lmlm,col="red")
abpos29_fix %>% filter(delta>480)

lmlm_oppp <- lm(rank.x ~ delta,data=abpos29_fix)
summary(lmlm_oppp)
plot(rank.y ~ delta,data=abpos29_fix)
abline(lmlm_oppp,col="red")



ggplot(abpos29_fix,aes(x=rank.x ,y=delta))+geom_point()+
  stat_smooth(method=lm)+
  xlab("네이버 랭크") + ylab("판매량") +
  theme_classic()+
  theme(axis.title.y=element_text(angle=0, size=14),axis.title.x=element_text(angle=0, size=14))
  

cor(abpos29_fix$rank.x,abpos29_fix$delta,method='pearson')
cor.test(abpos29_fix$rank.x,abpos29_fix$delta)
