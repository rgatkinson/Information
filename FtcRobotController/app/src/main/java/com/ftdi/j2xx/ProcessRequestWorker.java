package com.ftdi.j2xx;

import android.util.Log;

class ProcessRequestWorker implements Runnable
    {
    int bufferCount;
    private ProcessInCtrl processInCtrl;

    ProcessRequestWorker(ProcessInCtrl processInCtrl)
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
                InBuffer inBuffer = this.processInCtrl.acquireReadableBuffer(iBuffer);

                if (inBuffer.getLength() > 0)
                    {
                    this.processInCtrl.processBulkInData(inBuffer);
                    inBuffer.purge();
                    }

                this.processInCtrl.releaseWritableBuffer(iBuffer);
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
