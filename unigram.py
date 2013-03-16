#!/usr/bin/python

import sys
import math

def getwordtokens(corpusFileName):
	lines = tuple(open(corpusFileName, 'r'))
	tokList = []
	for line in lines:
		tokList.extend(line.split())
	return tokList

def calcunigram(corpusFileName):
	unigram={}
	tokList = getwordtokens(corpusFileName)
	totalCount = len(tokList)
	for tok in tokList:
		if unigram.has_key(tok):
			unigram[tok] += 1
		else:
			unigram[tok] = 1
	for word, count in unigram.iteritems():
		unigram[word] = float(count)/float(totalCount)
	return unigram
	
def displayDict(unigram):
	for key, value in unigram.iteritems():
		print key + "\t" + str(value)
		
def loadunigram(fileName):
	lines = tuple(open(fileName, 'r'))
	unigram = {}
	for line in lines:
		items = line.split()
		unigram[items[0]] = float(items[1])
	return unigram

def loglikelihood(corpusFileName, lmFileName):
	unigram = loadunigram(lmFileName)
	tokList = getwordtokens(corpusFileName)
	loglik = float(0)
	for tok in tokList:
		loglik += math.log(unigram[tok], 2)
	return loglik/len(tokList)

def calcperplexity(loglikelihood):
	return math.pow(2, loglikelihood * (-1))

def main():
	try:
		flag = sys.argv[1]
		if flag == "perplexity":
			corpusFileName = sys.argv[2]
			lmFileName = sys.argv[3]
			loglik = loglikelihood(corpusFileName, lmFileName)
			perplx = calcperplexity(loglik)
			print "average log-likelihood = " + str(loglik)
			print "perplexity = " + str(perplx)
		elif flag == "unigram":
			corpusFileName = sys.argv[2]
			unigram = calcunigram(corpusFileName)
			displayDict(unigram)
		else:
			print "unknown flag."
			return
	except:
		print "usage:"
		print "\tcase 1:"
		print "\t\tunigram.py unigram  corpusFileName > lanModelFileName"
		print "\tcase 2:"
		print "\t\tunigram.py perplexity corpusFileName lanModelFileName"

if __name__ == "__main__":
	main()
