
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd


def computAccuracy(csv_path, windowSize):
    df = pd.read_csv(csv_path)
    df =  df['appname']
    # print(df.shape[0])

    START = windowSize              # 起始
    T1, T3, T5 = [], [], []
    right1, right3, right5 = [], [], []
    for start in range(START):
        T1.append(0)
        T3.append(0)
        T5.append(0)
        if start%2==1:
            right1.append(1)
            right3.append(1)
            right5.append(1)
        else:
            right1.append(0)
            right3.append(0)
            right5.append(0)
        
    
    rightWin = 41   # 平滑准确率
    display = 50   # 显示多长时间的准确率

    for Indx in range(df.shape[0]-display, df.shape[0]-1):

        dct, dct2 = {}, {}
        for i in range(Indx-START, Indx):
            dct2[df[i]] = dct2.get(df[i], 0) + 1
            if df[i] == df[Indx]:
                dct[df[i+1]] = dct.get(df[i+1], 0) + 1

        Sdct = sorted(dct.items(), key=lambda val:val[1],reverse=True)
        Sdct2 = sorted(dct2.items(), key=lambda val:val[1],reverse=True)

        out = []
        i = 0
        # 某App后面出现最多
        # print('#'*64)
        for item in Sdct[:5]:
            if item[1] > 1:
                out.append(item[0])
                # print(str(item[0]) + " : " + str(item[1]))
                i += 1
        # if str(df[Indx+1]) in out[:i]:
        #     tmp += 1

        # 出现最多
        out1 = []
        j = 0
        for item in Sdct2[:5]:
            if item[0] not in out and i+j<5:
                out1.append(item[0])
                # print(str(item[0]) + " : " + str(item[1]))
                j += 1

        tmp = out[:i]+out1[:j]
        if str(df[Indx+1]) in tmp[0]:
            right1.append(1)
        else:
            right1.append(0)
        if str(df[Indx+1]) in tmp[:3]:
            right3.append(1)
        else:
            right3.append(0)
        if str(df[Indx+1]) in tmp:
            right5.append(1)
        else:
            right5.append(0)
        
        Accuracy_tony = 0.
        for i in range(rightWin):
            Accuracy_tony += right1[-i-1]
        T1.append(Accuracy_tony/rightWin)

        Accuracy_tony = 0.
        for i in range(rightWin):
            Accuracy_tony += right3[-i-1]
        T3.append(Accuracy_tony/rightWin)

        Accuracy_tony = 0.
        for i in range(rightWin):
            Accuracy_tony += right5[-i-1]
        T5.append(Accuracy_tony/rightWin)

    # print(right5)
    # plt.figure(figsize=(32, 20))
    # plt.plot(T1, label='Top1')
    # plt.plot(T3, label='Top3')
    # plt.plot(T5, label='Top5')
    # plt.title("App's Prediction")
    # plt.xlabel('Windows Lenght')
    # plt.ylabel('Prediction Accuracy')
    # plt.legend()
    # plt.show()

    return T1[-25:], T3[-25:], T5[-25:]



def predictionApp(currentApp, csv_path, windowSize):

    df = pd.read_csv(csv_path)
    df =  df['appname']
    # print(df.shape[0])

    windowSize = windowSize

    dct, dct2 = {}, {}
    for i in range(df.shape[0]-windowSize, df.shape[0]-1):
        dct2[df[i]] = dct2.get(df[i], 0) + 1
        if df[i] == currentApp:
            dct[df[i+1]] = dct.get(df[i+1], 0) + 1

    Sdct = sorted(dct.items(), key=lambda val:val[1],reverse=True)
    Sdct2 = sorted(dct2.items(), key=lambda val:val[1],reverse=True)
    # print(Sdct[0][1])

    out = []
    i = 0
    # 某App后面出现最多
    for item in Sdct[:5]:
        if item[1] > 1:
            out.append(item[0])
            # print(str(item[0]) + " : " + str(item[1]))
            i += 1
    # 出现最多
    for item in Sdct2[:5]:
        if item[0] not in out:
            out.append(item[0])
            # print(str(item[0]) + " : " + str(item[1]))
            i += 1

    # computAccuracy(df, windowSize)
    # findWindowsSize(df)
    # findJiterSize(df)

    return out[:5]


def findWindowsSize(df):
    W = []
    kaishi, jiesu, step = 180, 240, 10
    for win in range(kaishi, jiesu, step):

        START = win              # 起始
        T5 = []
        for start in range(START+1):
            T5.append(0)

        dct, dct2 = {}, {}
        right5 = 0.

        for i in range(0, START):
            dct2[df[i]] = dct2.get(df[i], 0) + 1
            if df[i] == df[START]:
                dct[df[i+1]] = dct.get(df[i+1], 0) + 1

        for Indx in range(START+1, df.shape[0]-1):

            Sdct = sorted(dct.items(), key=lambda val:val[1],reverse=True)
            Sdct2 = sorted(dct2.items(), key=lambda val:val[1],reverse=True)

            out = []
            i = 0
            # 某App后面出现最多
            # print('#'*64)
            for item in Sdct[:5]:
                if item[1] > 10:
                    out.append(item[0])
                    # print(str(item[0]) + " : " + str(item[1]))
                    i += 1
            # 出现最多
            for item in Sdct2[:5]:
                if item[0] not in out and i<5:
                    out.append(item[0])
                    # print(str(item[0]) + " : " + str(item[1]))
                    i += 1


            if df[Indx] in out[:5]:
                right5 += 1.
            
            if df[Indx-1-win] == df[Indx-1] and df[Indx-1-win] in dct:
                dct[df[Indx-1-win]] -= 1
                if dct[df[Indx-1-win]] == 0:
                    dct.pop(df[Indx-1- win])
            dct[df[Indx-1]] = dct.get(df[Indx-1], 0) + 1

            dct2[df[Indx-1-win]] -= 1
            # if dct2[df[Indx-1-win]] == 0:
            #     dct2.pop(df[Indx-1-win])
            dct2[df[Indx-1]] = dct2.get(df[Indx-1], 0) + 1

            print("第 ", Indx, " 个时间点的Top5 Accuracy :  ", right5/(Indx-START))
            T5.append(right5/(Indx-START))

        W.append(T5)

    plt.figure(figsize=(32, 20))
    for win_indx in range(len(W)):
        plt.plot(W[win_indx], label='Top5___' + str(win_indx*step +kaishi))

    plt.title("App's Prediction")
    plt.xlabel('Windows Lenght')
    plt.ylabel('Prediction Accuracy')
    plt.legend()
    plt.show()



def findJiterSize(df):
    W = []
    for win in range(0, 10):

        START = 190              # 起始
        T5 = []
        for start in range(START+1):
            T5.append(0)

        dct, dct2 = {}, {}
        right5 = 0.

        for i in range(0, START):
            dct2[df[i]] = dct2.get(df[i], 0) + 1
            if df[i] == df[START]:
                dct[df[i+1]] = dct.get(df[i+1], 0) + 1

        for Indx in range(START+1, df.shape[0]-1):

            Sdct = sorted(dct.items(), key=lambda val:val[1],reverse=True)
            Sdct2 = sorted(dct2.items(), key=lambda val:val[1],reverse=True)

            out = []
            i = 0
            # 某App后面出现最多
            # print('#'*64)
            for item in Sdct[:5]:
                if item[1] > win:
                    out.append(item[0])
                    # print(str(item[0]) + " : " + str(item[1]))
                    i += 1
            # 出现最多
            for item in Sdct2[:5]:
                if item[0] not in out and i<5:
                    out.append(item[0])
                    # print(str(item[0]) + " : " + str(item[1]))
                    i += 1


            if df[Indx] in out[:5]:
                right5 += 1.
            
            if df[Indx-1-START] == df[Indx-1] and df[Indx-1-START] in dct:
                dct[df[Indx-1-START]] -= 1
                if dct[df[Indx-1-START]] == 0:
                    dct.pop(df[Indx-1- START])
            dct[df[Indx-1]] = dct.get(df[Indx-1], 0) + 1

            dct2[df[Indx-1-START]] -= 1
            if dct2[df[Indx-1-START]] == 0:
                dct2.pop(df[Indx-1-START])
            dct2[df[Indx-1]] = dct2.get(df[Indx-1], 0) + 1

            print("第 ", Indx, " 个时间点的Top5 Accuracy :  ", right5/(Indx-START))
            T5.append(right5/(Indx-START))

        W.append(T5)

    plt.figure(figsize=(32, 20))
    for win_indx in range(len(W)):
        plt.plot(W[win_indx], label='Top5___' + str(win_indx))

    plt.title("App's Prediction")
    plt.xlabel('Windows Lenght')
    plt.ylabel('Prediction Accuracy')
    plt.legend()
    plt.show()



if __name__ == "__main__":
    # list = predictionApp("微信", "test.csv")
    computAccuracy("test.csv", 235)
    # print(list)
    # changeWindowsSize()
