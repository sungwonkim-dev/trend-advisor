library(tidyverse)
getwd()
df25 <- read.csv("data/health_20190725.csv")
df26 <- read.csv("data/health_20190726.csv")
df27 <- read.csv("data/health_20190727.csv")

df25 <- rename(df25,
               purchase25 = purchase,
               review25 = review)
df26 <- rename(df26,
               purchase26 = purchase,
               review26 = review)
df27 <- rename(df27,
               purchase27 = purchase,
               review27 = review)

j <- full_join(df25,df26,by=c('item','item'))
j <- rename(j,
            rank25 = rank.x,
            rank26 = rank.y)
j <- full_join(j,df27,by=c('item','item'))
j <- rename(j,
            rank27 = rank)
j$delta_p1 <- j$purchase26-j$purchase25
j$delta_r1 <- j$review26-j$review25
j$delta_p2 <- j$purchase27-j$purchase26
j$delta_r2 <- j$review27-j$review26

j <- arrange.vars(j,c("rank25"=2))
write.csv(j,file="join_mac.csv",row.names = F,fileEncoding = "UTF-8")
write.csv(j,file="join_windows.csv",row.names = F)

delta <- j %>% select(item,delta_p1,delta_r1,delta_p2,delta_r2)

write.csv(delta,file="delta1.csv",row.names = F,fileEncoding = "UTF-8")

df$pur_re_rate <- df$review/df$purchase
str(df)
df1 <- df
df1 <- arrange(df1,desc(pur_re_rate))



arrange.vars <- function(data, vars){
  ##stop if not a data.frame (but should work for matrices as well)
  stopifnot(is.data.frame(data))
  
  ##sort out inputs
  data.nms <- names(data)
  var.nr <- length(data.nms)
  var.nms <- names(vars)
  var.pos <- vars
  ##sanity checks
  stopifnot( !any(duplicated(var.nms)), 
             !any(duplicated(var.pos)) )
  stopifnot( is.character(var.nms), 
             is.numeric(var.pos) )
  stopifnot( all(var.nms %in% data.nms) )
  stopifnot( all(var.pos > 0), 
             all(var.pos <= var.nr) )
  
  ##prepare output
  out.vec <- character(var.nr)
  out.vec[var.pos] <- var.nms
  out.vec[-var.pos] <- data.nms[ !(data.nms %in% var.nms) ]
  stopifnot( length(out.vec)==var.nr )
  
  ##re-arrange vars by position
  data <- data[ , out.vec]
  return(data)
}


na.rm=T
boxplot(df25$purchase25)$stats
ggplot(df25,aes(x=rank,y=purchase25))+geom_point()
dddd <- full_join(df25,df26,by=c('item','item'))
dddd$del_p <- dddd$purchase26-dddd$purchase25
ggplot(dddd,aes(x=rank.x,y=del_p))+geom_point()
