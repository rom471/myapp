from datetime import datetime


import pandas as pd
from flask import Flask, redirect, request
from flask import render_template
from model import predictionApp, computAccuracy


app = Flask(__name__)

windowSize = 139
csv_path = "test.csv"
#一开始就计算准去率

@app.route('/', methods=['POST','GET'])
def home_page():
    top1, top3, top5= computAccuracy(csv_path, windowSize)
    return render_template('index.html',top1=top1,top3=top3,top5=top5)


#处理单个app
@app.route('/send', methods=['POST'])
def handle_send():
    current=request.form['appname']
    print(current)
    list=getTop5(current, test.csv, windowSize)
    str=','.join(list)
    
    return str

#根据当前应用，给出5个推荐应用
#你需要重写此方法！！！
def getTop5(currentApp, csv_path, windowSize):
    # list=[]
    # list.append(currentApp)
    # list.append("菜鸟")
    # list.append("时钟")
    # list.append("手机淘宝")
    # list.append("拼多多")
    
    return predictionApp(currentApp, csv_path, windowSize)

#处理用户上传的app列表
@app.route('/sends', methods=['POST'])
def handle_sends():
    appnames=request.form['appnames'].split(',')
    print(appnames)
    # timestamps=request.form['timestamps'].split(',')
    # time_str=[]
    # for t in timestamps:
    #     t=int(t)/1000
    #     s=datetime.fromtimestamp(t).strftime('%Y-%m-%d %H:%M:%S')
    #     time_str.append(s)
    # dataframe = pd.DataFrame({'appname':appnames})
    # dataframe.to_csv("test.csv",index=False,sep=',')
    dataframe=appnames
    with open("./test.csv", "a", encoding="utf-8") as fout:
        for line in dataframe:
            if line != "appname" and line != "":
                fout.write(line + '\n')
    
    current=appnames[-1]
    csv_path = "test.csv"
    list=getTop5(current, csv_path, windowSize)
    list=','.join(list)
    # print(list)
    #如果接收到了非空数据，需要返回以分号结尾的字符串
    if len(appnames)>0:
        list+=";"
    return list
    # return "Done"
#处理用户上传的all app列表
@app.route('/sends_all', methods=['POST'])
def handle_sends_all():
    appnames=request.form['appnames'].split(',')
    print(appnames)
    timestamps=request.form['timestamps'].split(',')
    time_str=[]
    for t in timestamps:
        t=int(t)/1000
        s=datetime.fromtimestamp(t).strftime('%Y-%m-%d %H:%M:%S')
        time_str.append(s)
    dataframe = pd.DataFrame({'appname':appnames,'time':time_str})
    dataframe.to_csv("origin.csv",index=False,sep=',')

    return "Done"

if __name__ == '__main__':
    app.run(host='0.0.0.0',
    port=5000,
    debug=True)
