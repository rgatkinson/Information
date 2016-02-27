package com.ftdi.j2xx;

import android.util.Log;

class ProcessRequestThread implements Runnable
    {
    int bufferCount;
    private ProcessInCtrl processInCtrl;

    ProcessRequestThread(ProcessInCtrl processInCtrl)
        {
        this.processInCtrl = processInCtrl;
        this.bufferCount = this.processInCtrl.getDriverParameters().getBufferCount();
        }

    public void run()
        {
        int iBuffer = 0;

        try
            {
            do
                {
                SomeKindOfBuffer someKindOfBuffer = this.processInCtrl.getOtherAcquireBuffer(iBuffer);

                if (someKindOfBuffer.getSomeCount() > 0)
                    {
                    this.processInCtrl.processSomeKindOfAvailability(someKindOfBuffer);
                    someKindOfBuffer.clear();
                    }

                this.processInCtrl.releaseBuffer(iBuffer);
                iBuffer = (iBuffer + 1) % this.bufferCount;
                } while (!Thread.interrupted());

            throw new InterruptedException();
            }
        catch (InterruptedException e)
            {
            Log.d("ProcessRequestThread::", "Device has been closed.");
            e.printStackTrace();
            }
        catch (Exception e)
            {
            Log.e("ProcessRequestThread::", "Fatal error!");
            e.printStackTrace();
            }
        }
    }
