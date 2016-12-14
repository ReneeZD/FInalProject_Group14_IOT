from machine import ADC, PWM, Pin, I2C, RTC
import time, socket, gc, ure

# p0=Pin(12,Pin.OUT)
# p2=Pin(14,Pin.OUT)

# p1=Pin(0,Pin.OUT)
# p1.high()
# p3=Pin(0,Pin.OUT)
pwm1 = PWM(Pin(0))
en=Pin(15,Pin.OUT)
en.high()
s0=Pin(16,Pin.OUT)
s1=Pin(14,Pin.OUT)
s2=Pin(12,Pin.OUT)
s3=Pin(13,Pin.OUT)
import ssd1306
i2c = I2C(scl=Pin(5), sda=Pin(4), freq=100000)
x=ssd1306.SSD1306_I2C(128,32,i2c)

# p12=Pin(12,Pin.OUT)
# p0.low()
# p2.low()
adc=ADC(0)
def do_connect():
    import network
    sta_if = network.WLAN(network.STA_IF)
    if not sta_if.isconnected():
        print('connecting to network...')
        sta_if.active(True)
        # sta_if.connect('TC8715D60', 'TC8715DB26D60')
        sta_if.connect('Columbia University', '')
        while not sta_if.isconnected():
            pass
    print('network config:', sta_if.ifconfig())
do_connect()

def display(status,distance):
    if(status==0):
        posture="good posture"
    elif(status==1):
        posture="humpback"
    elif(status==2):
        posture="lean back"
    if(distance==0):
        eye="good distance"
    else:
        eye="sitting too close"
    if(status==0 & distance==0):
        pwm1.freq(500)
        pwm1.duty(0)
    else:
        pwm1.freq(500)
        pwm1.duty(512)
    x.fill(0)
    x.text(posture,1,1)
    x.text(eye,1,16)
    x.show()

def post(d0,d1,d2,d3,d4,d5):
    global status
    host = 'ec2-54-149-75-206.us-west-2.compute.amazonaws.com'
    path = 'posture/post'
    addr = socket.getaddrinfo(host, 80)[0][-1]
    s1 = socket.socket()
    s1.connect(addr)
    j_data='{"distance0":' + str(d0) + ', "distance1":' + str(d1)  + ', "distance2":' + str(d2) + ', "distance3":' + str(d3) + ', "distance4":' + str(d4) + ', "distance5":' + str(d5) + '}'
    # '{"distance0":' + str(d0) + ', "distance1":' + str(d1)  + ', "distance2":' + str(d2) + ', "distance3":' + str(d3) + ', "distance4":' + str(d4) + ', "distance5":' + str(d5) + '}'
    length = len(j_data)
    s1.send(b'POST /%s HTTP/1.1\r\nHost: %s\r\nContent-Type: application/json\r\nContent-Length: %s\r\nCache-Control: no-cache\r\n\r\n%s' % (path,host,length,j_data))
    data=s1.recv(400)
    print(data)
    distance = ure.search('"distance": (.*),', data)
    status = ure.search('"posturejudge": (.*)}', data)
    # print("distance",distance.group(1))
    # print("status",status.group(1))
    display(int(status.group(1)),int(distance.group(1)))
    # print(status,distance)
    s1.close()

def enc0():
    en.low()
    s0.low()
    s1.low()
    s2.low()
    s3.low()

def enc1():
    en.low()
    s0.high()
    s1.low()
    s2.low()
    s3.low()

def enc2():
    en.low()
    s0.low()
    s1.high()
    s2.low()
    s3.low()

def enc3():
    en.low()
    s0.high()
    s1.high()
    s2.low()
    s3.low()

def enc4():
    en.low()
    s0.low()
    s1.low()
    s2.high()
    s3.low()

def enc5():
    en.low()
    s0.high()
    s1.low()
    s2.high()
    s3.low()

d0=1024
d1=1024
d2=1024
d3=1024
d4=1024
d5=1024
t=1/7
rtc = RTC()
rtc.datetime((2016,12,30,4,0,0,0,0))
while(1):
    #read from c0
    enc0()
    time.sleep(2*t)
    print("c0:")
    d0=adc.read()
    print(d0)
    # post(adc.read())
    en.high()
    #read from c1
    enc1()
    time.sleep(t)
    print("c1:")
    d1=adc.read()
    print(d1)
    # post(adc.read())
    en.high()
    #read from c2
    enc2()
    time.sleep(t)
    print("c2:")
    d2=adc.read()
    print(d2)
    # post(adc.read())
    en.high()
    #read from c3
    enc3()
    time.sleep(t)
    print("c3:")
    d3=adc.read()
    print(d3)
    # post(adc.read())
    en.high()
    #read from c4
    enc4()
    time.sleep(t)
    print("c4:")
    d4=adc.read()
    print(d4)
    # post(adc.read())
    en.high()
    #read from c5
    enc5()
    time.sleep(t)
    print("c5:")
    d5=adc.read()
    print(d5)
    post(d0,d1,d2,d3,d4,d5)
    en.high()

    a = rtc.datetime()
    print("a6")
    print(a[5])
    if(a[5]>55):
        pwm1.freq(500)
        pwm1.duty(512)
        x.fill(0)
        x.text("take a walk!",1,1)
        x.show()
    gc.collect()


# # enc0()
# while(1):
#     print ("value:")
#     print (adc.read())
#     time.sleep_ms(200)
