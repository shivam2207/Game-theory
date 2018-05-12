#include <bits/stdc++.h>
using namespace std;
bool compare(string a, string b)
{
	if(a.size()<b.size())
		return true;
	else if (a.size()>b.size())
		return false;
	else
	{
		int i=0;
		while(i<(int)a.size())
		{
			if(a[i]==b[i])
				i++;
			else if(a[i]<b[i])
				return true;
			else
				return false;
		}
		return true;
	}
}
class Genetate
{
	vector <string> str;
public:
	vector <string> playerscombination(int n)
	{
		string s;
		recursive(s,1,n);
		sort(str.begin(),str.end());
		sort(str.begin(),str.end(),compare);
		return str;
	}
private:
	void recursive(string s,int m,int n)
	{
		for(int i=m;i<=n;i++)
		{
			s.push_back(i+64);
			recursive(s,i+1,n);
			s.pop_back();
		}
		if(s.size()>0)
			str.push_back(s);
	}
};
class Coalition
{
public:
	vector <string> players;
	map <string,double> v;
	Coalition(vector<string> &players,map <string,double> v)
	{
		this->players=players;
		this->v=v;
	}
	void shapleyvalues(void)
	{
		long long int factorial[players.size()+1];
		factorial[0]=1;
		factorial[1]=1;
		for(int i=2;i<=(int)players.size();i++)
		{
			factorial[i]=factorial[i-1]*i;
		}
		for(auto i: players)
		{
			double payoff=0.0,contribution=v[i];
			cout << i<<"'s origional contribution: "<<(double)contribution<<endl;
			for(int j=1;j<=(int)players.size();j++)
			{
				double p=(double)factorial[j-1]*factorial[players.size()-j]/(double)factorial[players.size()];
				auto itr=v.begin();
				while(itr!=v.end())
				{
					string coal=itr->first;
					if(j==1)
					{
						// cout << i<<" is joining 1st thus getting a payoff: "<<(double)v[i]<<" weighted by: "<<p<<endl;
						payoff=p*(double)v[i];
						break;
					}
					else
					{
						if((int)coal.size()==(j-1))
						{
							if(coal.find(i)==string::npos)
							{
								string coal_with_me=coal+i;
								sort(coal_with_me.begin(),coal_with_me.end());
								// cout << i <<" is joining: "<<coal<<" To make it: "<<coal_with_me;
								// cout<<" Thus getting: "<<(double)v[coal_with_me]-(double)v[coal]<<" weighted by: "<<p<<endl;
								payoff=payoff+p*((double)v[coal_with_me]-(double)v[coal]);
							}
						}
					}
					itr++;
				}
			}
			cout <<"Shapley value for "<<i<<" :"<<payoff<<endl;
			cout<<"---------------------------------------------------------------"<<endl;
		}
	}
};
int main()
{
	double value;
	map <string,double> v;
	vector<string> players,results;
	cout <<"Enter the number of players"<<endl;
	int n,i;
	cin >> n;
	Genetate object;
	results=object.playerscombination(n);
	for(i=0;i<n;i++)
		players.push_back(results[i]);
	cout <<"Enter the values" << endl;
	for(i=1;i<pow(2,n);i++)
	{
		cin >> value;
		v[results[i-1]]=value;
	}
	Coalition obj(players,v);
	obj.shapleyvalues();
	return 0;
}