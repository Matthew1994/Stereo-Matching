
## 1. 开发环境
#### 1.1 操作系统
```
Linux Deepin 
```

#### 1.2 语言平台
```
Java / Eclipse
```

## 2. 目录结构
```
.
├── [2005 CVPR] Locally Adaptive Support-Weight Approach for Visual Correspondence Search.pdf
├── bin
│   └── StereoMatching
│       ├── ASW.class
│       ├── boot.class
│       ├── Evaluate.class
│       ├── NCC.class
│       └── SSD.class
├── proj-stereo-matching.pdf
├── result                                                  ----保存的结果文件夹
│   ├── asw.txt
│   ├── ncc.txt
|   ├── ssd.txt
|   ├── ssd_i_10.txt                                        ----强度增强后的ssd/ncc坏点率   
|   ├── ncc_i_10.txt
│   ├── IAdd10                                              ----强度增强后的ssd/ncc图像
│   |   ├── Aloe
│   |   │   ├── Aloe_disp1_NCC.png
│   |   │   ├── Aloe_disp1_SSD.png
│   |   │   ├── Aloe_disp5_NCC.png
│   |   │   └── Aloe_disp5_SSD.png
│   |   ├── ...
│   |   └── Wood2
│   |       ├── Wood2_disp1_NCC.png
│   |       ├── Wood2_disp1_SSD.png
│   |       ├── Wood2_disp5_NCC.png
│   |       └── Wood2_disp5_SSD.png
|   |
│   └── output                                              ----ssd/asw/ncc处理后图像
│       ├── Aloe
│       │   ├── Aloe_disp1_ASW.png
│       │   ├── Aloe_disp1_NCC.png
│       │   ├── Aloe_disp1_SSD.png
│       │   ├── Aloe_disp5_ASW.png
│       │   ├── Aloe_disp5_NCC.png
│       │   └── Aloe_disp5_SSD.png
│       ├── ...
│       └── Wood2
│           ├── Wood2_disp1_ASW.png
│           ├── Wood2_disp1_NCC.png
│           ├── Wood2_disp1_SSD.png
│           ├── Wood2_disp5_ASW.png
│           ├── Wood2_disp5_NCC.png
│           └── Wood2_disp5_SSD.png
│   
├── src
│   └── StereoMatching
│       ├── ASW.java
│       ├── boot.java
│       ├── Evaluate.java
│       ├── NCC.java
│       └── SSD.java
└── test                                                    ----测试图像文件夹(左右眼)
    ├── Aloe
    │   ├── disp1.png
    │   ├── disp5.png
    │   ├── view1.png
    │   └── view5.png
    ├── ...
    └── Wood2
        ├── disp1.png
        ├── disp5.png
        ├── view1.png
        └── view5.png


```

## 3. 匹配算法

#### 3.1 SSD
>3.1.1 定义

    像素差的平方和(Sum of Squared Difference),指将滑动窗口与匹配窗口对应的像素点差的平方然后相加作为匹配开销.
>3.1.2 公式

    SSD(u,v) =  Sum{[Left(u,v) - Right(u,v)] * [Left(u,v) - Right(u,v)]} 

>3.1.3 步骤

    1.构造一个小窗口，类似与卷积核。

    2.用窗口覆盖左边的图像，选择出窗口覆盖区域内的所有像素点。 

    3.同样用窗口覆盖右边的图像并选择出覆盖区域的像素点。

    4.左边覆盖区域减去右边覆盖区域，并求出所有像素点差的平方然后相加求和。

    5.移动右边图像的窗口，重复3，4的动作。（这里有个搜索范围，超过这个范围跳出） 

>3.1.4 结果(坏点率)


| 名称         | 左视差          |   右视差        |
| ----------- |  ----------    |  ----------    |
|Plastic      |  0.83475816    |  0.84227845    |  
|Baby1        |  0.37956286    |  0.37599634    |  
|Wood2        |  0.56457906    |  0.56689655    |  
|Midd2        |  0.72099792    |  0.72756757    |  
|Cloth3       |  0.17213040    |  0.15430034    |  
|Baby2        |  0.42537792    |  0.43200052    |  
|Wood1        |  0.48587143    |  0.47580578    |  
|Rocks2       |  0.25442925    |  0.23836566    |  
|Cloth1       |  0.10600169    |  0.08710869    |  
|Bowling1     |  0.72634001    |  0.72998898    |  
|Aloe         |  0.24250269    |  0.25482625    |  
|Baby3        |  0.46971365    |  0.46675119    |  
|Lampshade2   |  0.76531428    |  0.76038325    |  
|Flowerpots   |  0.62571588    |  0.62840002    |  
|Lampshade1   |  0.64363024    |  0.63590288    |  
|Midd1        |  0.64346992    |  0.64081953    |  
|Monopoly     |  0.70718687    |  0.69307547    |  
|Cloth4       |  0.24237563    |  0.25981524    |  
|Cloth2       |  0.34997815    |  0.35099557    |  
|Bowling2     |  0.54952108    |  0.54728205    |  
|Rocks1       |  0.25687122    |  0.25166932    | 

</center>


#### 3.2 NCC
>3.2.1 定义

        归一化互相关(Normalized Cross Correlation method, NCC)匹配算法是一种经典的统计匹配算法，通过
    计算模板图像和匹配图像的互相关值，来确定匹配的程度。

>3.2.2 公式

<center>![](https://raw.githubusercontent.com/Matthew1994/pushfile/master/stereomatch-img/ncc.png)</center>
>3.2.3 步骤

    1.构造一个小窗口，类似与卷积核。

    2.用窗口覆盖左边的图像，选择出窗口覆盖区域内的所有像素点。 

    3.同样用窗口覆盖右边的图像并选择出覆盖区域的像素点。

    4.求出左右两个窗口像素转灰度值后的均值,然后在遍历一次窗口,代入公式。

    5.移动右边图像的窗口，重复3，4的动作。（这里有个搜索范围，超过这个范围跳出） 

>3.2.4 结果(坏点率)

| 名称         | 左视差          |   右视差        |
| ----------- |  ----------    |  ----------    |
|Plastic      |  0.67342662    |  0.67366302    |  
|Baby1        |  0.18463451    |  0.18666972    |  
|Wood2        |  0.22718857    |  0.23233924    |  
|Midd2        |  0.56747253    |  0.56575587    |  
|Cloth3       |  0.14965973    |  0.13322315    |  
|Baby2        |  0.25467574    |  0.26574177    |  
|Wood1        |  0.23811580    |  0.21058608    |  
|Rocks2       |  0.18189507    |  0.16732591    |  
|Cloth1       |  0.10653315    |  0.08790589    |  
|Bowling1     |  0.37919502    |  0.37961631    |  
|Aloe         |  0.23816697    |  0.24369264    |  
|Baby3        |  0.28041314    |  0.27678892    |  
|Lampshade2   |  0.43432370    |  0.43674552    |  
|Flowerpots   |  0.36283629    |  0.36822933    |  
|Lampshade1   |  0.43321266    |  0.43174583    |  
|Midd1        |  0.57224063    |  0.56849753    |  
|Monopoly     |  0.48530901    |  0.44881947    |  
|Cloth4       |  0.19138631    |  0.21146620    |  
|Cloth2       |  0.19376443    |  0.19923850    |  
|Bowling2     |  0.28881093    |  0.28947593    |  
|Rocks1       |  0.18868680    |  0.18222576    |  

#### 3.3 ASW

>3.3.1 定义

        自适应权重(Adaptive Support Weight),核心思想是为匹配窗口中的每个像素赋予一个权值，权值是根据它们与窗
    口中心点的颜色差和距离得到的。本质上是完成了一种近似的图像分割。
>3.3.2 公式

<center>![](https://raw.githubusercontent.com/Matthew1994/pushfile/master/stereomatch-img/asw_1.png) -------(1)</center>
<center>![](https://raw.githubusercontent.com/Matthew1994/pushfile/master/stereomatch-img/asw_2.png)  -------(2)</center>
<center>![](https://raw.githubusercontent.com/Matthew1994/pushfile/master/stereomatch-img/asw_3.png)  -------(3)</center>
<center>![](https://raw.githubusercontent.com/Matthew1994/pushfile/master/stereomatch-img/asw_4.png)  -------(4)</center>

>3.3.3 步骤

    1. 构造一个小窗口，类似与卷积核。

    2. 用窗口覆盖左边的图像，选择出窗口覆盖区域内的所有像素点。 

    3. 同样用窗口覆盖右边的图像并选择出覆盖区域的像素点。

    4. 将窗口的像素转为Lab模式,求出两个窗口各个像素的Lab值的欧式距离c(p,q),如公式(4)。

    5. 计算窗口选中的像素与中心像素的欧式距离g(p,q),与c(p,q)一起代入公式(3),作为该像素的权重.
        (PS:这里的Rc,Rg是经验值, 不同的取值效果也不一样,我用的是Rc = 45,Rp = 5)

    6. 两个窗口对应的像素rgb对应的差的绝对值相加,如公式(2)

    7. 遍历窗口的各个点,代入公式(1)中,计算最后的匹配开销.

    8. 移动右边图像的窗口，重复3，4的动作。（这里有个搜索范围，超过这个范围跳出） 

>3.3.4 结果

| 名称         | 左视差          |   右视差        |
| ----------- |  ----------    |  ----------    |
|Plastic      |  0.81406939    |  0.82257364    |  
|Baby1        |  0.33896342    |  0.33584190    |  
|Wood2        |  0.51007766    |  0.50534327    |  
|Midd2        |  0.70115236    |  0.70696763    |  
|Cloth3       |  0.14840236    |  0.13087044    |  
|Baby2        |  0.40480335    |  0.41162882    |  
|Wood1        |  0.43273996    |  0.42360873    |  
|Rocks2       |  0.24306518    |  0.22706518    |  
|Cloth1       |  0.10314343    |  0.08428933    |  
|Bowling1     |  0.69791302    |  0.70408970    |  
|Aloe         |  0.22452687    |  0.23639471    |  
|Baby3        |  0.45015152    |  0.44545117    |  
|Lampshade2   |  0.74019724    |  0.73434867    |  
|Flowerpots   |  0.59357412    |  0.59400087    |  
|Lampshade1   |  0.61264590    |  0.60355783    |  
|Midd1        |  0.61463528    |  0.61293810    |  
|Monopoly     |  0.68494906    |  0.67546824    |  
|Cloth4       |  0.22144061    |  0.23975407    |  
|Cloth2       |  0.30409463    |  0.30669746    |  
|Bowling2     |  0.48556525    |  0.48720029    |  
|Rocks1       |  0.24967886    |  0.24305246    |  

## 4. 实验结果分析
#### 4.1 概述
        通过上面算法结果的三个表格的对比,从整体上来看NCC的效果最佳,ASW其次,SSD的效果最差.NCC和ASW,在每类测试中得出的结果都
    比要SSD更精确,而且大部分样例的测试坏点率都要低50%以上,而NCC和ASW相比,ASW只有Cloth3,Cloth1,Aloe这三个样例比NCC效果更
    好,而且坏点率都仅仅少1%以内.

#### 4.2 NCC和SSD比较
    
    NCC比SSD效果好非常多.尤其是当右图的强度增加了10之后,NCC并没有太大变化,但是SSD就和原来相差非常多,SSD的具体数据如下:

      SSD                    强度不变                        强度加10

 名称         | 左视差          |   右视差       | |  左视差        |   右视差  
 ----------- |  ----------    |  ----------    | | ---------     |  --------
Plastic      |  0.83475816    |  0.84227845    | | 0.93323110    |  0.92872021
Baby1        |  0.37956286    |  0.37599634    | | 0.83595314    |  0.87937308
Wood2        |  0.56457906    |  0.56689655    | | 0.94931345    |  0.95221497
Midd2        |  0.72099792    |  0.72756757    | | 0.82118800    |  0.81305613
Cloth3       |  0.17213040    |  0.15430034    | | 0.56931752    |  0.54610150
Baby2        |  0.42537792    |  0.43200052    | | 0.80088999    |  0.82623519
Wood1        |  0.48587143    |  0.47580578    | | 0.92636466    |  0.93250340
Rocks2       |  0.25442925    |  0.23836566    | | 0.68855326    |  0.67078537
Cloth1       |  0.10600169    |  0.08710869    | | 0.29090673    |  0.25139024
Bowling1     |  0.72634001    |  0.72998898    | | 0.91414868    |  0.92138181
Aloe         |  0.24250269    |  0.25482625    | | 0.51647573    |  0.49986075
Baby3        |  0.46971365    |  0.46675119    | | 0.87659101    |  0.87476653
Lampshade2   |  0.76531428    |  0.76038325    | | 0.94840522    |  0.95291805
Flowerpots   |  0.62571588    |  0.62840002    | | 0.95861834    |  0.95594656
Lampshade1   |  0.64363024    |  0.63590288    | | 0.91516759    |  0.92562262
Midd1        |  0.64346992    |  0.64081953    | | 0.83580355    |  0.82802092
Monopoly     |  0.70718687    |  0.69307547    | | 0.60091514    |  0.59631505
Cloth4       |  0.24237563    |  0.25981524    | | 0.54642657    |  0.56864116
Cloth2       |  0.34997815    |  0.35099557    | | 0.70018101    |  0.70576743
Bowling2     |  0.54952108    |  0.54728205    | | 0.84623269    |  0.85125374
Rocks1       |  0.25687122    |  0.25166932    | | 0.69589189    |  0.68050238

    我挑出坏点率相差最大的Baby1出来看一下:

    Baby1 原图(左眼图片,右眼图片):


<center>![](https://raw.githubusercontent.com/Matthew1994/Stereo-Matching/master/test/Baby1/view1.png)    ![](https://raw.githubusercontent.com/Matthew1994/Stereo-Matching/master/test/Baby1/view5.png)</center>

    左视差图强度增强前与强度增强后:
<center>![](https://raw.githubusercontent.com/Matthew1994/Stereo-Matching/master/result/output/Baby1/Baby1_disp1_SSD.png)    ![](https://raw.githubusercontent.com/Matthew1994/Stereo-Matching/master/result/IAdd10/Baby1/Baby1_disp1_SSD.png)</center>

    从原理上说,SSD只是单纯地比较对应的像素值,因此光线对SSD的影响非常大,两个完全一样的窗口,其中一个的亮度发生改变,那么匹配代价就
    会变得很大,而NCC在计算互相关系数的时候做了归一化操作,相当于重新标定,这样就可以排除光线线性变换带来的影响,所以当左右图的亮度相
    差越大,那么用NCC处理出来视差图就比SSD的更精确.

#### 4.3 ASW和NCC比较
        ASW在每一类测试中虽然比SSD好,但是却没有NCC好,但是从网上找到的资料看,理论上ASW应该比NCC更强大,因为ASW考虑了窗口里每个点
    到中心的欧式距离,这样也符合常理,匹配的时候越靠边缘的像素偏差往往越大.而且ASW在比较色彩的时候用的是Lab模型,也就是分别考虑了明度
    和色调(这比NCC更科学,本质上NCC只是比较色调).
        但是我的实验结果并没有体现到ASW的强大,除了Cloth3,Cloth1,Aloe这三个样例比NCC效果好一点点之外(坏点率相差不超过1%),其余样
    例都比不上NCC.我觉得出现这种情况的原因主要有以下三点:
        (1) 经验值Rc,Rp的取值.
            正如上文3.3.2 公式所示,ASW算法的实现中涉及3个经验值(Rc, Rp, k), 其中k能在后面的运算中全部约掉,所以它的作用仅仅对编
        程中保证精度起作用,对结果影响不大.而Rc和Rp决定了颜色与距离所占的比重,对结果有比较大的影响.我经过多次试验,选取了Rc = 45,
        Rp = 5,这样的搭配得出的效果比较好,但是我相信这并不是最好的参数,由于ASW运算一次耗时较长,所以我并没有找出效果最好的参数.
        (2) 匹配窗口的大小
            模板窗口的大小对ASW的影响也是挺大.在我的实现中,我采取了5*5大小的模板窗口.我也对AOE样例进行过3*3,5*5,11*11,33*33这
        4种规模的窗口,得出的结果是33*33的坏点率是最低的,但是5*5,11*11的与它也相差不到1%,而且33*33规模的窗口处理一个样例的一张图
        就要一个小时.当然仅仅凭借AOE这一个样例的结果来判断参数的优劣是不科学的,按照正常的实验步骤,应该把所有的样例都跑一次,然后求出
        平均值.但是正如上面说到的,33*33大小窗口的耗时实在非常长,所以还没来得及都跑一次.
        (3) 样例的选取
            这次总共提供了21个样例,数目比较少,所以仅仅凭这21个样例的结果并不能说明NCC与ASW的优劣.