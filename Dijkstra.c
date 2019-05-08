#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<ctype.h>
#define M 9999


typedef struct car
{
    int id;
    int star;
    int end;
    int speed;
    int time;
}CAR;
typedef struct road
{
    int id;
    int length;
    int speed;
    int channel;
    int from;
    int to;
    int isDuplex;
}ROAD;
typedef struct cross
{
    int id;
    int id1;
    int id2;
    int id3;
    int id4;
}CROSS;
typedef struct jz
{
    float qz;
    int r_id;
    int c_idi;
    int c_idj;
    int v;
}JZ;
typedef struct answer
{
    int car_id;
    int re_time;
    int r_id[300];
}AS;


//----------------------------------------------------------------------------------
//求最短路径------------------------------------------------------------------------
float *Dijkstra(CAR car,JZ A[200][200],int c,float *d_ist)
{
    float dist[200][200+2];   //到该点最短路径及长度及点数
    int prev[200];  //记录前驱结点
    int s=-1;   // 记录新加最短经过点
    int n=c;
    int i,j,k,p;
    int sp=M;    //限速速度
    int starti=0,endj=0;

    //找出起始点和重点路口对应邻接矩阵的下标
    for(i=0;i<n;i++)
    {
        for(j=0;j<n;j++)
        {
            if(car.star==A[i][j].c_idi&&car.end==A[i][j].c_idj)
            {
                starti=i;
                endj=j;
            }
        }
    }


    for(i=0; i<n; ++i)
    {
        dist[i][c] = A[starti][i].qz;
        dist[i][0] = starti;    //出发点
        dist[i][1] = i;      //终止点
        dist[i][c+1]=2;    //当前初始点到终点的点数
        if(dist[i][c] == M)
            prev[i] = -1;
        else
            prev[i] = starti;
        if(car.speed>=A[starti][i].v)//找最小限速
            sp=A[starti][i].v;
        else
            sp=car.speed;
        dist[i][c] = A[starti][i].qz/sp;    //目前出发点到终止点的距离
    }
    for(i=1;i<n;i++)
    {
        for(j=0;j<n;j++)     //初始点到终点
        {
            for(k=0;k<n;k++)     //判断初始点到终点是否存在新加点使到终点的路径更短
            {
                if(prev[k]!=-1)
                {
                    if(car.speed>=A[k][j].v)     //限速最小的速度
                        sp=A[k][j].v;
                    else
                        sp=car.speed;
                    if(A[k][j].qz!=M&&starti!=j&&dist[j][c]>dist[k][c]+A[k][j].qz/sp)   //判断是否有加入点使得路径比原来的路径更短
                    {
                        prev[j]=starti;                                //初始点能到j点
                        dist[j][c]=dist[k][c]+A[k][j].qz/sp;    //更新到j点的最短路径
                        s=k;       //记录新加的点
                    }
                }
            }
            if(s!=-1)                             //判断是否存在新加点
            {
                for(p=0;p<dist[s][c+1];p++)  //加入更新路径
                    dist[j][p]=dist[s][p];
                dist[j][p]=j;   //将终点后移
                dist[j][c+1]=dist[s][c+1]+1;     //记录到终点所有点数
                s=-1;
            }
        }
    }
    d_ist=dist[endj];
    return d_ist;
}


//-------------------------------------------------------------------------------
//判断两个路口是否相通-----------------------------------------------------------
int cr_ro_id(CROSS a,CROSS b)
{
    int id=0;
    if(a.id1!=-1&&b.id1!=-1&&a.id1==b.id1)
    {
        id=a.id1;
    }
    if(a.id1!=-1&&b.id2!=-1&&a.id1==b.id2)
    {
        id=a.id1;
    }
    if(a.id1!=-1&&b.id3!=-1&&a.id1==b.id3)
    {
        id=a.id1;
    }
    if(a.id1!=-1&&b.id4!=-1&&a.id1==b.id4)
    {
        id=a.id1;
    }
    if(a.id2!=-1&&b.id1!=-1&&a.id2==b.id1)
    {
        id=a.id2;
    }
    if(a.id2!=-1&&b.id2!=-1&&a.id2==b.id2)
    {
        id=a.id2;
    }
    if(a.id2!=-1&&b.id3!=-1&&a.id2==b.id3)
    {
        id=a.id2;
    }
    if(a.id2!=-1&&b.id4!=-1&&a.id2==b.id4)
    {
        id=a.id2;
    }
    if(a.id3!=-1&&b.id1!=-1&&a.id3==b.id1)
    {
        id=a.id3;
    }
    if(a.id3!=-1&&b.id2!=-1&&a.id3==b.id2)
    {
        id=a.id3;
    }
    if(a.id3!=-1&&b.id3!=-1&&a.id3==b.id3)
    {
        id=a.id3;
    }
    if(a.id3!=-1&&b.id4!=-1&&a.id3==b.id4)
    {
        id=a.id3;
    }
    if(a.id4!=-1&&b.id1!=-1&&a.id4==b.id1)
    {
        id=a.id4;
    }
    if(a.id4!=-1&&b.id2!=-1&&a.id4==b.id2)
    {
        id=a.id4;
    }
    if(a.id4!=-1&&b.id3!=-1&&a.id4==b.id3)
    {
        id=a.id4;
    }
    if(a.id4!=-1&&b.id4!=-1&&a.id4==b.id4)
    {
        id=a.id4;
    }
    return id;
}



//--------------------------------------------------------------------------
//--------------------------------------------------------------------------
int main()
{
    int i,j,k,p=0,a=0,r=0,c=0;
    float *dist;
    CAR car[120000];
    ROAD road[300];
    CROSS corss[200];
    JZ A[200][200];
    AS ans;
    FILE *fp;
    if((fp = fopen("car.txt","r"))==NULL)
    {
        printf("can not open file\n");
    }
    fscanf(fp,"#(id,from,to,speed,planTime)\n");
    do
    {
        fscanf(fp,"(%d, %d, %d, %d, %d)\n",&car[a].id,&car[a].star,&car[a].end,&car[a].speed,&car[a].time);
        a++;
    }while(feof(fp)==0);
    fclose(fp);

    if((fp = fopen("road.txt","r"))==NULL)
    {
        printf("can not open file\n");
    }
    fscanf(fp,"#(id,length,speed,channel,from,to,isDuplex)\n");
    do
    {
        fscanf(fp,"(%d, %d, %d, %d, %d, %d, %d)\n",&road[r].id,&road[r].length,&road[r].speed,&road[r].channel,&road[r].from,&road[r].to,&road[r].isDuplex);
        r++;
    }while(feof(fp)==0);
    fclose(fp);

    if((fp = fopen("cross.txt","r"))==NULL)
    {
        printf("can not open file\n");
    }
    fscanf(fp,"#(id,roadId,roadId,roadId,roadId)\n");
    do
    {
        fscanf(fp,"(%d, %d, %d, %d, %d)\n",&corss[c].id,&corss[c].id1,&corss[c].id2,&corss[c].id3,&corss[c].id4);
        c++;

    }while(feof(fp)==0);
    fclose(fp);

    //初始化A
    for(i=0;i<c;i++)
    {
        for(j=0;j<c;j++)
        {
            A[i][j].qz=M;
            A[i][j].r_id=0;
            A[i][j].v=M;
            A[i][j].c_idi=corss[i].id;
            A[i][j].c_idj=corss[j].id;
        }
    }
    //给邻接矩阵id赋值
    for(i=0;i<c;i++)
    {
        for(j=0;j<c;j++)
        {
            if(i!=j)
            {
                k=cr_ro_id(corss[i],corss[j]);//判断两个路口是否相邻
                if(k!=0)
                {
                    A[i][j].r_id=k;
                }
            }
        }
    }

    //判断两个路口间道路是否双向
    for(i=0;i<c;i++)
    {
        for(j=i+1;j<c;j++)
        {
            for(k=0;k<r;k++)
            {
                if(A[i][j].r_id==road[k].id)
                {
                    A[i][j].qz=road[k].length;
                    A[j][i].qz=road[k].length;
                    A[i][j].v=road[k].speed;
                    A[j][i].v=road[k].speed;
                }
                if(road[k].isDuplex!=1&&A[i][j].r_id==road[k].id)
                {
                    if(road[k].from==corss[i].id)
                    {
                        A[j][i].r_id=0;
                        A[j][i].qz=M;
                    }
                    else
                    {
                        A[i][j].r_id=0;
                        A[i][j].qz=M;
                    }
                }
            }
        }
    }

    if((fp = fopen("answer.txt","a"))==NULL)
    {
        printf("can not open file\n");
        exit(0);
    }
    fprintf(fp,"#(carId,StartTime,RoadId...)\n");


    //将结果存入as中
    for(i=0;i<a;i++)
    {
        for(p=0;p<c;p++)
        {
            ans.r_id[p]=0;
        }
        dist=Dijkstra(car[i],A,c,dist);
        ans.car_id=car[i].id;
        ans.re_time=car[i].time;
        for(j=0;j<dist[c+1]-1;j++)
        {
            ans.r_id[j]=A[(int)dist[j]][(int)dist[j+1]].r_id;
        }
        k=0;
        fprintf(fp,"(%d, %d",ans.car_id,ans.re_time);
        while(k<dist[c+1]-1)
        {
            fprintf(fp," ,%d",ans.r_id[k]);
            k++;
        }
        fprintf(fp,")\n");
    }

    fclose(fp);

    return 0;
}





