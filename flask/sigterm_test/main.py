import signal
import time

run = True

def handler_stop_signals(signum, frame):
    global run
    run = False
    print("got sigterm")
    for i in range(1000):
        time.sleep(1)
        print(i)

signal.signal(signal.SIGTERM, handler_stop_signals)

while run:
    print("hello!")
    time.sleep(1)
    pass # do stuff including other IO stuff
