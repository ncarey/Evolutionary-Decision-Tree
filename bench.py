import subprocess

cmd = "cd /home/ncarey/gitrepos/Evolutionary-Decision-Tree; ./run_traditional.sh splice gainRatio"

avgT = 0.0
avgA = 0.0
avgP = 0.0
avgR = 0.0

for i in range(0,20):
  a = subprocess.Popen(cmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT).stdout.read()
  a = a.split("\n")[1]
  b = a.split(",")
  avgT += float(b[0])
  avgA += float(b[1])
  avgP += float(b[2])
  avgR += float(b[3])
  
avgT = avgT / 20
avgA = avgA / 20
avgP = avgP / 20
avgR = avgR / 20

print "acc: {0}".format(avgA)
print "pre: {0}".format(avgP)
print "rec: {0}".format(avgR)
print "sec: {0}".format(avgT)
