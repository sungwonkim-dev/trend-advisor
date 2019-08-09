df29 <- rename(a,
               r29=rank,
               p29=purchase)
df30 <- rename(b,
               r30=rank,
               p30=purchase)
df31 <- rename(c,
               r31=rank,
               p31=purchase)

df <- inner_join(df29,df30,by=c("keyword"="keyword","title"="title"))
df <- inner_join(df,df31,by=c("keyword"="keyword","title"="title"))
df <- as.data.frame(df)
str(df)

df <- arrange.vars(df,c("title"=2))

df_bu <- df
df$p30_29 <- df$p30 - df$p29
df$p31_29 <- df$p31 - df$p30

df$r30_29 <- df$r30 - df$r29
df$r31_29 <- df$r31 - df$r30

df$delta <- df$p31_29-df$p30_29

ggplot(df,aes(x=r30_29,y=delta))+geom_point()+
  stat_smooth(method=lm)

ggplot(df,aes(x=r31_29,y=delta))+geom_point()+
  stat_smooth(method=lm)

table(df$p30_29)
table(df$p31_29)


df_kw <- df_bu %>% group_by(keyword) %>% summarise(r29=mean(r29),p29=sum(p29),
                                                   r30=mean(r30),p30=sum(p30),
                                                   r31=mean(r31),p31=sum(p31))
df_kw <- arrange(df_kw,r29)
str(df_kw)
df_kw$pur30 <- df_kw$p30-df_kw$p29
df_kw$pur31 <- df_kw$p31-df_kw$p30
df_kw$r_delta2930 <- df_kw$r30-df_kw$r29
df_kw$r_delta3031 <- df_kw$r31-df_kw$r30

df_kw$p_delta <- df_kw$pur31-df_kw$pur30
ggplot(df_kw,aes(x=r_delta3031,y=p_delta))+geom_point()+
  stat_smooth(method=lm)
cor.test(df_kw$r_delta3031,df_kw$p_delta)



#df_pos <- df %>% filter(p30_29>=0,p31_29>=0)
df_pos <- df %>% filter(p30_29>0,p31_29>0)
df_pos <- df_pos %>% group_by(keyword) %>% summarise(r30_29=mean(r30_29),p30_29=sum(p30_29),
                                                   r31_30=mean(r31_29),p31_30=sum(p31_29))
df_pos$p_delta <- df_pos$p31_30-df_pos$p30_29
ggplot(df_pos,aes(x=r31_30,y=p_delta))+geom_point()+
  stat_smooth(method=lm)
cor.test(df_kw$r_delta3031,df_kw$p_delta)


df_kw <- arrange(df_kw,p_delta)
